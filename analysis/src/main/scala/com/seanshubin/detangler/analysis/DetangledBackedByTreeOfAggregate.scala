package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model._

class DetangledBackedByTreeOfAggregate(level: Int, treeOfAggregate: Tree[Aggregate]) extends Detangled {
  override def root(): Standalone = ???

  override def reasonsFor(standalone: Standalone): Set[Reason] = ???

  override def cycleParts(cycle: Cycle): Set[Standalone] = lookupMetrics(cycle).cycleParts

  override def isLeaf(standalone: Standalone): Boolean = ???

  override def dependedOnBy(module: Module): Set[Standalone] = lookupMetrics(module).dependedOnBy

  private def lookupMetrics(module: Module): Metrics = {
    if (module.isRoot) {
      Metrics.Empty
    } else {
      val aggregate = treeOfAggregate.value(module.parent.path)
      val metrics = aggregate.modules(module)
      metrics
    }
  }

  override def childStandalone(standalone: Standalone): Set[Standalone] = ???

  override def transitive(module: Module): Int = lookupMetrics(module).transitive

  override def cycleSize(cycle: Cycle): Int = ???

  override def childModules(standalone: Standalone): Set[Module] = ???

  override def dependsOn(module: Module): Set[Standalone] = lookupMetrics(module).dependsOn

  override def levelsDeep: Int = level

  override def depth(module: Module): Int = lookupMetrics(module).depth

  override def breadth(module: Module): Int = lookupMetrics(module).breadth
}
