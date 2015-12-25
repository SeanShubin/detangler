package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model._

class DetangledBackedByAggregate(aggregate: Aggregate) extends Detangled {
  override def reasonsFor(standalone: Standalone): Set[Reason] = ???

  override def cycleParts(cycle: Cycle): Set[Standalone] = aggregate.modules(cycle).cycleParts

  override def isLeaf(standalone: Standalone): Boolean = ???

  override def dependedOnBy(module: Module): Set[Standalone] = aggregate.modules(module).dependedOnBy

  override def childStandalone(standalone: Standalone): Set[Standalone] = ???

  override def transitive(module: Module): Int = aggregate.modules(module).transitiveDependencies.size

  override def cycleSize(cycle: Cycle): Int = ???

  override def childModules(standalone: Standalone): Set[Module] = aggregate.modules.keySet

  override def dependsOn(module: Module): Set[Standalone] = aggregate.modules(module).dependsOn

  override def levelsDeep: Int = 1

  override def breadth(module: Module): Int = aggregate.modules(module).breadth

  override def depth(module: Module): Int = aggregate.modules(module).depth
}
