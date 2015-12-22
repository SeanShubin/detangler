package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Detangled, Standalone}

class DetanglerImpl(cycleFinder: CycleFinder[Standalone]) extends Detangler {
  override def analyze(data: Seq[(Standalone, Standalone)]): Detangled = {
    val dependsOnMap = data.foldLeft(Map[Standalone, Set[Standalone]]())(CollectionUtil.appendPairToMapFromKeyToSetOfValues)
    analyze(dependsOnMap)
  }

  def analyze(originalDependsOnMap: Map[Standalone, Set[Standalone]]): Detangled = {
    val originalDependedOnByMap = CollectionUtil.invert(originalDependsOnMap)
    val dependsOnMap = CollectionUtil.addEmptyKeysForMapOfSets(originalDependsOnMap)
    val dependedOnByMap = CollectionUtil.addEmptyKeysForMapOfSets(originalDependedOnByMap)
    val cycles: Map[Standalone, Set[Standalone]] = cycleFinder.findCycles(dependsOnMap)
    val emptyAggregate = Aggregate(Map(), dependsOnMap, dependedOnByMap, cycles)
    val aggregate = dependsOnMap.keys.foldLeft(emptyAggregate)(Aggregate.add)
    val detangled = new DetangledBackedByAggregate(aggregate)
    detangled
  }
}
