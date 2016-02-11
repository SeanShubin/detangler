package com.seanshubin.detangler.report

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.detangler.model._

class DetangledFake(theRoot: Standalone, map: Map[Module, ModuleInfo], val levelsDeep: Int) extends Detangled {
  override def childModules(standalone: Standalone): Seq[Module] = map(standalone).children.toSeq.sortWith(Compare.lessThan(Module.compare))

  override def childStandalone(standalone: Standalone): Seq[Standalone] = childModules(standalone).flatMap {
    case x: Standalone => Some(x)
    case _ => None
  }

  override def cycleSize(cycle: Cycle): Int = map(cycle).parts.size

  override def cycleParts(cycle: Cycle): Seq[Standalone] = map(cycle).parts.toSeq

  override def partOfCycle(standalone: Standalone): Option[Cycle] = map(standalone).partOfCycle

  override def breadth(module: Module): Int = map(module).dependsOn.size

  override def depth(module: Module): Int = map(module).depth

  override def transitive(module: Module): Int = map(module).complexity

  override def dependsOn(module: Module): Seq[Standalone] =
    map(module).dependsOn.filter(hasParentOf(module.parent)).toSeq

  override def dependedOnBy(module: Module): Seq[Standalone] =
    map(module).dependedOnBy.filter(hasParentOf(module.parent)).toSeq

  private def hasParentOf(parent: Standalone): Standalone => Boolean = (child) => child.path.init == parent.path

  override def reasonsFor(standalone: Standalone): Seq[Reason] = reasonsFor(childStandalone(standalone))

  override def isLeaf(standalone: Standalone): Boolean = standalone.path.size == levelsDeep

  override def plainDependsOnFor(standalone: Standalone): Map[String, Set[String]] = Map()

  override def plainCyclesFor(standalone: Standalone): Map[String, Set[String]] = Map()

  override def cycles(): Seq[Cycle] = Seq()

  override def entryPoints(): Seq[Standalone] = ???

  override def plainEntryPointsFor(standalone: Standalone): Set[String] = Set()

  override def contains(standalone: Standalone): Boolean = ???

  private def reasonsFor(parts: Seq[Standalone]): Seq[Reason] = {
    reasonsFor(parts, parts)
  }

  private def reasonsFor(leftParts: Seq[Standalone], rightParts: Seq[Standalone]): Seq[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- map(fromPart).dependsOn
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(childStandalone(fromPart), childStandalone(toPart)))
    }
  }
}
