package com.seanshubin.detangler.core

case class Detangled(map: Map[UnitId, UnitInfo]) {
  def topLevelUnits(): Set[UnitId] = {
    map.keySet.filter(_.paths.size == 1)
  }

  def topLevelArrows(): Seq[Arrow] = {
    arrowsFor(topLevelUnits())
  }

  def arrowsFor(parts: Set[UnitId]): Seq[Arrow] = {
    arrowsFor(parts, parts)
  }

  private def arrowsFor(leftParts: Set[UnitId], rightParts: Set[UnitId]): Seq[Arrow] = {
    for {
      fromPart <- leftParts.toSeq.sorted
      toPart <- map(fromPart).dependsOn.toSeq.sorted
      if rightParts.contains(toPart)
    } yield {
      Arrow(fromPart, toPart, arrowsFor(map(fromPart).composedOf, map(toPart).composedOf))
    }
  }
}
