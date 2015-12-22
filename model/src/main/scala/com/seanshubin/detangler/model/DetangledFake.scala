package com.seanshubin.detangler.model

class DetangledFake(theRoot: Standalone, map: Map[Module, ModuleInfo], val levelsDeep: Int) extends Detangled {
  override def root(): Standalone = theRoot

  override def childModules(standalone: Standalone): Set[Module] = map(standalone).children

  override def childStandalone(standalone: Standalone): Set[Standalone] = childModules(standalone).flatMap {
    case x: Standalone => Some(x)
    case _ => None
  }

  override def cycleSize(cycle: Cycle): Int = map(cycle).parts.size

  override def cycleParts(cycle: Cycle): Set[Standalone] = map(cycle).parts

  override def depth(module: Module): Int = map(module).depth

  override def complexity(module: Module): Int = map(module).complexity

  override def dependsOn(module: Module): Set[Standalone] =
    map(module).dependsOn.filter(hasParentOf(module.parent))

  override def dependedOnBy(module: Module): Set[Standalone] =
    map(module).dependedOnBy.filter(hasParentOf(module.parent))

  override def reasonsFor(standalone: Standalone): Set[Reason] = reasonsFor(childStandalone(standalone))

  override def isLeaf(standalone: Standalone): Boolean = standalone.path.size == levelsDeep

  private def hasParentOf(parent: Standalone): Standalone => Boolean = (child) => child.path.init == parent.path

  private def reasonsFor(parts: Set[Standalone]): Set[Reason] = {
    reasonsFor(parts, parts)
  }

  private def reasonsFor(leftParts: Set[Standalone], rightParts: Set[Standalone]): Set[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- map(fromPart).dependsOn
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(childStandalone(fromPart), childStandalone(toPart)))
    }
  }
}
