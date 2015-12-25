package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

case class DependencyData(level: Int,
                          all: Set[Standalone],
                          dependsOn: Map[Standalone, Set[Standalone]],
                          dependedOnBy: Map[Standalone, Set[Standalone]]) {
  def subsetFor(path: Seq[String]): DependencyData = {
    val newLevel = path.size + 1
    if (newLevel > level) {
      DependencyData.createEmpty(newLevel)
    } else {
      def isRelevant(standalone: Standalone): Boolean =
        standalone.path.startsWith(path)
      def isEntryRelevant(entry: (Standalone, Set[Standalone])): Boolean = {
        val (key, _) = entry
        isRelevant(key)
      }
      def filterDependencies(entry: (Standalone, Set[Standalone])): (Standalone, Set[Standalone]) = {
        val (key, oldValue) = entry
        val newValue = oldValue.filter(isRelevant)
        (key, newValue)
      }
      val newAll = all.filter(isRelevant)
      val newDependsOn = dependsOn.filter(isEntryRelevant).map(filterDependencies)
      val newDependedOnBy = dependedOnBy.filter(isEntryRelevant).map(filterDependencies)
      val filtered = DependencyData(level, newAll, newDependsOn, newDependedOnBy)
      filtered.atLevel(newLevel)
    }
  }

  def atLevel(targetLevel: Int): DependencyData = {
    if (level > targetLevel) {
      higherLevel().atLevel(targetLevel)
    } else {
      this
    }
  }

  def higherLevel(): DependencyData = {
    val newLevel = level - 1
    val newAll = all.map(_.parent)
    val newDependsOn = dependsOn.foldLeft(Map[Standalone, Set[Standalone]]())(promoteToParent)
    val newDependedOnBy = dependedOnBy.foldLeft(Map[Standalone, Set[Standalone]]())(promoteToParent)
    DependencyData(newLevel, newAll, newDependsOn, newDependedOnBy)
  }

  private def promoteToParent(soFar: Map[Standalone, Set[Standalone]], current: (Standalone, Set[Standalone])): Map[Standalone, Set[Standalone]] = {
    val (childKey, childValues) = current
    val key = childKey.parent
    def matchesKey(standalone: Standalone): Boolean = standalone == key
    val valuesToAdd = childValues.map(_.parent).filterNot(matchesKey)
    val newValues = soFar.get(key) match {
      case Some(oldValues) => oldValues ++ valuesToAdd
      case None => valuesToAdd
    }
    soFar + (key -> newValues)
  }
}

object DependencyData {
  type DependencyPair = (Standalone, Standalone)
  type DependencySeq = Seq[DependencyPair]
  type DependencyMap = Map[Standalone, Set[Standalone]]
  val emptyDependencies: DependencyMap = Map()

  def createEmpty(level: Int) = DependencyData(level = level, all = Set(), dependsOn = Map(), dependedOnBy = Map())

  def fromPairs(pairs: DependencySeq): DependencyData = {
    val all: Set[Standalone] = pairs.map(_._1).toSet ++ pairs.map(_._2).toSet
    val level = allSameLevel(all)
    val uncheckedDependsOn = pairs.foldLeft(emptyDependencies)(addDependency)
    val dependsOn = all.foldLeft(uncheckedDependsOn)(addEntryIfEmpty)
    val uncheckedDependedOnBy = pairs.map(reversePair).foldLeft(emptyDependencies)(addDependency)
    val dependedOnBy = all.foldLeft(uncheckedDependedOnBy)(addEntryIfEmpty)
    DependencyData(level, all, dependsOn, dependedOnBy)
  }

  def addDependency(soFar: DependencyMap, current: DependencyPair): DependencyMap = {
    val (from, to) = current
    val newDependencies = soFar.get(from) match {
      case Some(dependencies) => dependencies + to
      case None => Set(to)
    }
    soFar.updated(from, newDependencies)
  }

  def addEntryIfEmpty(dependencies: DependencyMap, entry: Standalone): DependencyMap = {
    if (dependencies.contains(entry)) dependencies else dependencies + (entry -> Set())
  }

  def reversePair(pair: DependencyPair): DependencyPair = (pair._2, pair._1)

  def allSameLevel(entries: Set[Standalone]): Int = {
    val firstLevel = entries.head.level
    def sameLevel(entry: Standalone): Boolean = entry.level == firstLevel
    if (entries.forall(sameLevel)) firstLevel
    else throw new RuntimeException("Data found at different granularity")
  }
}
