package com.seanshubin.detangler.core

trait Detangled {
  def depth(unitId: UnitId): Int

  def complexity(unitId: UnitId): Int

  def composedOf(unitId: UnitId): Seq[UnitId]

  def dependsOn(context: UnitId, unitId: UnitId): Seq[UnitId]

  def dependedOnBy(context: UnitId, unitId: UnitId): Seq[UnitId]

  def reasonsFor(unitId: UnitId): Seq[Reason]

  def reasonsFor(parts: Seq[UnitId]): Seq[Reason]

  def cycleSize(unitId: UnitId): Int
}
