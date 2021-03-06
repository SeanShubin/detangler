package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.detangler.model._

class DetangledBackedByTreeOfAggregate(level: Int,
                                       treeOfAggregate: Tree[Aggregate],
                                       allDependsOn: Map[Standalone, Set[Standalone]],
                                       entryPointSet: Set[Standalone]) extends Detangled {
  override def reasonsFor(standalone: Standalone): Seq[Reason] = reasonsFor(childStandalone(standalone))

  override def cycleParts(cycle: Cycle): Seq[Standalone] = sortByStandaloneInfo(lookupMetrics(cycle).cycleParts)

  override def allStandalone(): Seq[Standalone] = allStandalone(Standalone.Root)

  private def allStandalone(parent: Standalone): Seq[Standalone] = {
    val children = childStandalone(parent)
    if (children.isEmpty) {
      Seq(parent)
    } else {
      children.sortWith(Compare.lessThan(Standalone.compare)).flatMap(child => allStandalone(child))
    }
  }

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

  override def plainDependsOnFor(standalone: Standalone): Map[String, Set[String]] = {
    val aggregate = treeOfAggregate.value(standalone.path)
    val dependsOnStandalone = aggregate.dependsOnMap
    val dependsOn = toPlain(dependsOnStandalone)
    dependsOn
  }

  override def entryPoints(): Seq[Standalone] = entryPointSet.toSeq.sortWith(Compare.lessThan(entryPointCompare))

  override def plainCyclesFor(standalone: Standalone): Map[String, Set[String]] = {
    val aggregate = treeOfAggregate.value(standalone.path)
    val cyclesStandalone = aggregate.cycleMap
    val cycles = toPlain(cyclesStandalone)
    cycles
  }

  override def plainEntryPointsFor(standalone: Standalone): Set[String] = {
    def isChild(potentialChild: Standalone): Boolean = potentialChild.parent == standalone

    entryPointSet.filter(isChild).map(toPlain)
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

  override def cycles(): Seq[Cycle] =
    treeOfAggregate.breadthFirst().filter(_.hasCycle).flatMap(_.getCycles).sortWith(Compare.lessThan(cycleCompare))

  override def contains(standalone: Standalone): Boolean = {
    if (treeOfAggregate.contains(standalone.parent.path)) {
      val aggregate = treeOfAggregate.value(standalone.parent.path)
      aggregate.modules.contains(standalone)
    } else false
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

  private val entryPointCompare: (Standalone, Standalone) => Int = (left, right) => {
    def depthCompare(left: Standalone, right: Standalone): Int = depth(left).compareTo(depth(right))

    val depthThenStandaloneCompare = Compare.mergeCompareFunctions(Compare.reverse(depthCompare), Standalone.compare)
    depthThenStandaloneCompare(left, right)
  }

  private val cycleCompare: (Cycle, Cycle) => Int = (left, right) => {
    def levelCompare(left: Cycle, right: Cycle): Int = left.level.compareTo(right.level)

    def sizeCompare(left: Cycle, right: Cycle): Int = left.size.compareTo(right.size)

    def parentCompare(left: Cycle, right: Cycle): Int = left.parent.compareTo(right.parent)

    val mergedCompare = Compare.mergeCompareFunctions(levelCompare, Compare.reverse(sizeCompare), parentCompare, Cycle.compare)
    mergedCompare(left, right)
  }
}
