package com.seanshubin.detangler.core

trait Detangled {
  def depth(unitId: UnitId): Int

  def complexity(unitId: UnitId): Int

  def composedOf(unitId: UnitId): Seq[UnitId]

  def dependsOn(context: UnitId, unitId: UnitId): Seq[UnitId]

  def dependedOnBy(context: UnitId, unitId: UnitId): Seq[UnitId]

  def arrowsFor(unitId: UnitId): Seq[Arrow]

  def arrowsFor(parts: Seq[UnitId]): Seq[Arrow]

  def cycleSize(unitId: UnitId): Int
}
