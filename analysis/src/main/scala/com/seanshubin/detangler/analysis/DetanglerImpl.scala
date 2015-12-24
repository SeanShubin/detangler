package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Detangled, Standalone}

class DetanglerImpl(cycleFinder: CycleFinder[Standalone]) extends Detangler {
  override def analyze(data: Seq[(Standalone, Standalone)]): Detangled = {
    analyze(DependencyData.fromPairs(data))
  }

  def analyze(data: DependencyData): Detangled = {
    val tree = data.all.foldLeft(TreeOfStandalone.Empty)(TreeOfStandalone.addStandalone)
    val cycles: Map[Standalone, Set[Standalone]] = cycleFinder.findCycles(data.dependsOn)
    val emptyAggregate = Aggregate(Map(), data.dependsOn, data.dependedOnBy, cycles)
    val aggregate = data.dependsOn.keys.foldLeft(emptyAggregate)(Aggregate.add)
    val detangled = new DetangledBackedByAggregate(aggregate)
    detangled
  }
}
