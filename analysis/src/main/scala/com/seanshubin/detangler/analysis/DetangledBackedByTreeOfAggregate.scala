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

  override def partOfCycle(standalone: Standalone): Option[Cycle] = lookupMetrics(standalone).partOfCycle

  override def childModules(standalone: Standalone): Seq[Module] = sortByModuleInfo(treeOfAggregate.value(standalone.path).modules.keySet)

  override def dependsOn(module: Module): Seq[Standalone] = sortByStandaloneInfo(lookupMetrics(module).dependsOn)

  private def sortByStandaloneInfo(unsorted: Set[Standalone]): Seq[Standalone] = {
    unsorted.toSeq.map(lookupMetrics).sortWith(Compare.lessThan(Metrics.compare)).map(_.id.asInstanceOf[Standalone])
  }

  private def lookupMetrics(module: Module): Metrics = {
    if (module.isRoot) {
      Metrics.Empty
    } else {
      val aggregate = treeOfAggregate.value(module.parent.path)
      val metrics = aggregate.modules(module)
      metrics
    }
  }

  override def levelsDeep: Int = level

  override def depth(module: Module): Int = lookupMetrics(module).depth

  override def breadth(module: Module): Int = lookupMetrics(module).breadth

  override def plainDependsOnFor(standalone: Standalone): Map[String, Set[String]] = {
    val aggregate = treeOfAggregate.value(standalone.path)
    val dependsOnStandalone = aggregate.dependsOnMap
    val dependsOn = toPlain(dependsOnStandalone)
    dependsOn
  }

  private def toPlain(standalone: Map[Standalone, Set[Standalone]]): Map[String, Set[String]] = {
    standalone.map(toPlain)
  }

  private def toPlain(standalone: (Standalone, Set[Standalone])): (String, Set[String]) = {
    val (standaloneKey, standaloneValues) = standalone
    val key = toPlain(standaloneKey)
    val values = standaloneValues.map(toPlain)
    (key, values)
  }

  private def toPlain(standalone: Standalone): String = {
    standalone.path.last
  }

  override def plainCyclesFor(standalone: Standalone): Map[String, Set[String]] = {
    val aggregate = treeOfAggregate.value(standalone.path)
    val cyclesStandalone = aggregate.cycles
    val cycles = toPlain(cyclesStandalone)
    cycles
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
