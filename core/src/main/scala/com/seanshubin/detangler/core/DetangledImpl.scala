package com.seanshubin.detangler.core

case class DetangledImpl(map: Map[UnitId, UnitInfo]) extends Detangled {
  override def arrowsFor(unitId: UnitId): Seq[Arrow] = {
    arrowsFor(composedOf(unitId))
  }

  override def arrowsFor(parts: Seq[UnitId]): Seq[Arrow] = {
    arrowsFor(parts, parts)
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

  private def arrowsFor(leftParts: Seq[UnitId], rightParts: Seq[UnitId]): Seq[Arrow] = {
    for {
      fromPart <- leftParts
      toPart <- map(fromPart).dependsOn
      if rightParts.contains(toPart)
    } yield {
      Arrow(fromPart, toPart, arrowsFor(composedOf(fromPart), composedOf(toPart)))
    }
  }
}
