package com.seanshubin.detangler.core

case class DetangledImpl(map: Map[Module, ModuleInfo]) extends Detangled {
  override def reasonsFor(module: Module): Seq[Reason] = {
    reasonsFor(children(module))
  }

  override def reasonsFor(parts: Seq[Module]): Seq[Reason] = {
    reasonsFor(parts, parts)
  }

  override def depth(module: Module): Int = {
    map(module).depth
  }

  override def dependedOnBy(context: Module, module: Module): Seq[Module] = {
    map(module).dependedOnBy.filter(_.parent == context).toSeq.sorted
  }

  override def complexity(module: Module): Int = {
    map(module).complexity
  }

  override def dependsOn(context: Module, module: Module): Seq[Module] = {
    map(module).dependsOn.filter(_.parent == context).toSeq.sorted
  }

  override def children(module: Module): Seq[Module] = {
    map(module).children.toSeq.sorted
  }

  override def cycleParts(module: Module): Seq[Module] = {
    map(module).cycleParts.toSeq.sorted
  }

  override def cycleSize(module: Module): Int = cycleParts(module).size

  private def reasonsFor(leftParts: Seq[Module], rightParts: Seq[Module]): Seq[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- map(fromPart).dependsOn
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(children(fromPart), children(toPart)))
    }
  }
}
