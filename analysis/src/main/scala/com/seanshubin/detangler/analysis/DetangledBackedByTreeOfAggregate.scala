package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model._

class DetangledBackedByTreeOfAggregate(level: Int,
                                       treeOfAggregate: Tree[Aggregate],
                                       allDependsOn: Map[Standalone, Set[Standalone]]) extends Detangled {
  override def reasonsFor(standalone: Standalone): Set[Reason] = reasonsFor(childStandalone(standalone))

  override def cycleParts(cycle: Cycle): Set[Standalone] = lookupMetrics(cycle).cycleParts

  override def isLeaf(standalone: Standalone): Boolean = ???

  override def dependedOnBy(module: Module): Set[Standalone] = lookupMetrics(module).dependedOnBy

  override def childStandalone(standalone: Standalone): Set[Standalone] = {
    val allChildren = childModules(standalone)
    val standaloneChildren = allChildren.flatMap {
      case x: Standalone => Some(x)
      case _ => None
    }
    standaloneChildren
  }

  override def transitive(module: Module): Int = lookupMetrics(module).transitive

  override def cycleSize(cycle: Cycle): Int = lookupMetrics(cycle).cycleParts.size

  override def childModules(standalone: Standalone): Set[Module] = treeOfAggregate.value(standalone.path).modules.keySet

  override def dependsOn(module: Module): Set[Standalone] = lookupMetrics(module).dependsOn

  override def levelsDeep: Int = level

  override def depth(module: Module): Int = lookupMetrics(module).depth

  private def lookupMetrics(module: Module): Metrics = {
    if (module.isRoot) {
      Metrics.Empty
    } else {
      val aggregate = treeOfAggregate.value(module.parent.path)
      val metrics = aggregate.modules(module)
      metrics
    }
  }

  override def breadth(module: Module): Int = lookupMetrics(module).breadth

  private def reasonsFor(parts: Set[Standalone]): Set[Reason] = {
    reasonsFor(parts, parts)
  }

  private def reasonsFor(leftParts: Set[Standalone], rightParts: Set[Standalone]): Set[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- allDependsOn(fromPart)
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(childStandalone(fromPart), childStandalone(toPart)))
    }
  }

}
