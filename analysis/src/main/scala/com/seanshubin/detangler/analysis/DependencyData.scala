package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

case class DependencyData(level: Int,
                          all: Set[Standalone],
                          dependsOn: Map[Standalone, Set[Standalone]],
                          dependedOnBy: Map[Standalone, Set[Standalone]])

object DependencyData {
  type DependencyPair = (Standalone, Standalone)
  type DependencySeq = Seq[DependencyPair]
  type DependencyMap = Map[Standalone, Set[Standalone]]
  val emptyDependencies: DependencyMap = Map()

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
