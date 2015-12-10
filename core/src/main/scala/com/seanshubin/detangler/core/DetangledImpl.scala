package com.seanshubin.detangler.core

case class DetangledImpl(map: Map[UnitId, UnitInfo]) extends Detangled {
  override def reasonsFor(unitId: UnitId): Seq[Reason] = {
    reasonsFor(composedOf(unitId))
  }

  override def reasonsFor(parts: Seq[UnitId]): Seq[Reason] = {
    reasonsFor(parts, parts)
  }

  override def depth(unitId: UnitId): Int = {
    map(unitId).depth
  }

  override def dependedOnBy(context: UnitId, unitId: UnitId): Seq[UnitId] = {
    map(unitId).dependedOnBy.filter(_.parent == context).toSeq.sorted
  }

  override def complexity(unitId: UnitId): Int = {
    map(unitId).complexity
  }

  override def dependsOn(context: UnitId, unitId: UnitId): Seq[UnitId] = {
    map(unitId).dependsOn.filter(_.parent == context).toSeq.sorted
  }

  override def composedOf(unitId: UnitId): Seq[UnitId] = {
    map(unitId).composedOf.toSeq.sorted
  }

  override def cycleSize(unitId: UnitId): Int = composedOf(unitId).size

  private def reasonsFor(leftParts: Seq[UnitId], rightParts: Seq[UnitId]): Seq[Reason] = {
    for {
      fromPart <- leftParts
      toPart <- map(fromPart).dependsOn
      if rightParts.contains(toPart)
    } yield {
      Reason(fromPart, toPart, reasonsFor(composedOf(fromPart), composedOf(toPart)))
    }
  }
}
