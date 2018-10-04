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
    val newDependsOn = dependsOn.foldLeft(Map[Standalone, Set[Standalone]]())(DependencyData.promoteToParent)
    val newDependedOnBy = dependedOnBy.foldLeft(Map[Standalone, Set[Standalone]]())(DependencyData.promoteToParent)
    DependencyData(newLevel, newAll, newDependsOn, newDependedOnBy)
  }
}

object DependencyData {
  val emptyDependencies: Map[Standalone, Set[Standalone]] = Map()

  def createEmpty(level: Int) = DependencyData(level = level, all = Set(), dependsOn = Map(), dependedOnBy = Map())

  def fromMaps(dependsOn: Map[Standalone, Set[Standalone]],
               dependedOnBy: Map[Standalone, Set[Standalone]]): DependencyData = {
    val all = dependsOn.keySet
    val level = allSameLevel(all)
    DependencyData(level, all, dependsOn, dependedOnBy)
  }

  def allSameLevel(entries: Set[Standalone]): Int = {
    val firstLevel = entries.head.level

    def sameLevel(entry: Standalone): Boolean = entry.level == firstLevel

    if (entries.forall(sameLevel)) firstLevel
    else throw new RuntimeException("Data found at different granularity")
  }

  def addDependency(soFar: Map[Standalone, Set[Standalone]], current: (Standalone, Standalone)): Map[Standalone, Set[Standalone]] = {
    val (from, to) = current
    if (from == to) {
      soFar
    } else {
      val newDependencies = soFar.get(from) match {
        case Some(dependencies) => dependencies + to
        case None => Set(to)
      }
      soFar.updated(from, newDependencies)
    }
  }

  def addEntryIfEmpty(dependencies: Map[Standalone, Set[Standalone]], entry: Standalone): Map[Standalone, Set[Standalone]] = {
    if (dependencies.contains(entry)) dependencies else dependencies + (entry -> Set())
  }

  def reversePair(pair: (Standalone, Standalone)): (Standalone, Standalone) = (pair._2, pair._1)

  def promoteToParent(soFar: Map[Standalone, Set[Standalone]], current: (Standalone, Set[Standalone])): Map[Standalone, Set[Standalone]] = {
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

  def mergeAllHigherLevels(currentLevel: Int, original: Map[Standalone, Set[Standalone]]): Map[Standalone, Set[Standalone]] = {
    if (currentLevel == 0) {
      original
    } else {
      val promoted = original.foldLeft(Map[Standalone, Set[Standalone]]())(DependencyData.promoteToParent)
      val merged = original ++ promoted ++ mergeAllHigherLevels(currentLevel - 1, promoted)
      merged
    }
  }
}
