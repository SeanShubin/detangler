package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Detangled, Standalone}

class DetanglerImpl(cycleFinder: CycleFinder[Standalone]) extends Detangler {
  override def analyze(dependsOn: Map[Seq[String], Set[Seq[String]]], dependedOnBy: Map[Seq[String], Set[Seq[String]]]): Detangled = {
    val data = DependencyData.fromMaps(dependsOn, dependedOnBy)
    def analyzePath(value: Unit, path: Seq[String]): Aggregate = {
      val subset = data.subsetFor(path)
      val cycles: Map[Standalone, Set[Standalone]] = cycleFinder.findCycles(subset.dependsOn)
      val emptyAggregate = Aggregate(Map(), subset.dependsOn, subset.dependedOnBy, cycles)
      val aggregate = subset.dependsOn.keys.foldLeft(emptyAggregate)(Aggregate.add)
      aggregate
    }
    val treeOfPaths = data.all.map(_.path).foldLeft(Tree.Empty)(Tree.add)
    val treeOfAggregate = treeOfPaths.mapOverTree(analyzePath)
    val allDependsOn = DependencyData.mergeAllHigherLevels(data.level, data.dependsOn)
    val detangled = new DetangledBackedByTreeOfAggregate(data.level, treeOfAggregate, allDependsOn)
    detangled
  }
}
