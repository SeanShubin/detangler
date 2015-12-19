package com.seanshubin.detangler.report

import com.seanshubin.detangler.model._

class DetangledFake(theRoot: Single, map: Map[Module, ModuleInfo]) extends Detangled {
  override def root(): Single = theRoot

  override def childModules(single: Single): Set[Module] = map(single).children

  override def childSingles(single: Single): Set[Single] = childModules(single).flatMap {
    case x: Single => Some(x)
    case _ => None
  }

  override def cycleSize(cycle: Cycle): Int = map(cycle).parts.size

  override def cycleParts(cycle: Cycle): Set[Single] = map(cycle).parts

  override def depth(module: Module): Int = map(module).depth

  override def complexity(module: Module): Int = map(module).complexity

  override def dependsOn(single: Single): Set[Single] = map(single).dependsOn

  override def dependedOnBy(single: Single): Set[Single] = map(single).dependedOnBy

  override def reasonsFor(single: Single): Set[Reason] = reasonsFor(childSingles(single))

  private def hasParentOf(parent: Single): Single => Boolean = (child) => child.path.init == parent.path

  private def reasonsFor(parts: Set[Single]): Set[Reason] = {
    reasonsFor(parts, parts)
  }

  private def reasonsFor(leftParts: Set[Single], rightParts: Set[Single]): Set[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- map(fromPart).dependsOn
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(childSingles(fromPart), childSingles(toPart)))
    }
  }
}
