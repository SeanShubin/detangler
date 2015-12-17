package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Reason, Module, Single, Detangled}

class DetangledFake(theRoot:Single, map: Map[Module, ModuleInfo]) extends Detangled{
  override def root(): Single = theRoot

  override def childModules(single: Single): Set[Module] = map(single).children

  override def childSingles(single: Single): Set[Single] = childModules(single).flatMap {
    case x:Single => Some(x)
    case _ => None
  }

  override def depth(module: Module): Int = map(module).depth

  override def complexity(module: Module): Int = map(module).complexity

  override def dependsOn(single: Single): Set[Single] = map(single).dependsOn

  override def dependedOnBy(single: Single): Set[Single] = map(single).dependedOnBy

  override def reasonsFor(single: Single): Set[Reason] = reasonsFor(childSingles(single))

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
