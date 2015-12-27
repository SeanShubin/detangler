package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.detangler.model._

class DetangledBackedByTreeOfAggregate(level: Int,
                                       treeOfAggregate: Tree[Aggregate],
                                       allDependsOn: Map[Standalone, Set[Standalone]]) extends Detangled {
  override def reasonsFor(standalone: Standalone): Seq[Reason] = reasonsFor(childStandalone(standalone))

  override def cycleParts(cycle: Cycle): Seq[Standalone] = sortByStandaloneInfo(lookupMetrics(cycle).cycleParts)

  override def isLeaf(standalone: Standalone): Boolean = ???

  override def dependedOnBy(module: Module): Seq[Standalone] = sortByStandaloneInfo(lookupMetrics(module).dependedOnBy)

  private def sortByStandaloneInfo(unsorted: Set[Standalone]): Seq[Standalone] = {
    unsorted.toSeq.map(lookupMetrics).sortWith(Compare.lessThan(Compare.reverse(Metrics.compare))).map(_.id.asInstanceOf[Standalone])
  }

  override def childStandalone(standalone: Standalone): Seq[Standalone] = {
    val allChildren = childModules(standalone)
    val standaloneChildren = allChildren.flatMap {
      case x: Standalone => Some(x)
      case _ => None
    }
    standaloneChildren
  }

  override def transitive(module: Module): Int = lookupMetrics(module).transitive

  override def cycleSize(cycle: Cycle): Int = lookupMetrics(cycle).cycleParts.size

  override def childModules(standalone: Standalone): Seq[Module] = sortByModuleInfo(treeOfAggregate.value(standalone.path).modules.keySet)

  override def dependsOn(module: Module): Seq[Standalone] = sortByStandaloneInfo(lookupMetrics(module).dependsOn)

  override def levelsDeep: Int = level

  override def depth(module: Module): Int = lookupMetrics(module).depth

  override def breadth(module: Module): Int = lookupMetrics(module).breadth

  private def lookupMetrics(module: Module): Metrics = {
    if (module.isRoot) {
      Metrics.Empty
    } else {
      val aggregate = treeOfAggregate.value(module.parent.path)
      val metrics = aggregate.modules(module)
      metrics
    }
  }

  private def reasonsFor(parts: Seq[Standalone]): Seq[Reason] = {
    reasonsFor(parts, parts)
  }

  private def reasonsFor(leftParts: Seq[Standalone], rightParts: Seq[Standalone]): Seq[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- sortByStandaloneInfo(allDependsOn(fromPart))
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(childStandalone(fromPart), childStandalone(toPart)))
    }
  }

  private def sortByModuleInfo(unsorted: Set[Module]): Seq[Module] = {
    unsorted.toSeq.map(lookupMetrics).sortWith(Compare.lessThan(Metrics.compare)).map(_.id)
  }
}
