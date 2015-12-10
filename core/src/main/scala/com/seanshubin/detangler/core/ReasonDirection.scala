package com.seanshubin.detangler.core

import scala.collection.mutable.ArrayBuffer

sealed abstract case class ReasonDirection(caption: String) {
  ReasonDirection.valuesBuffer += this

  def name(left: UnitId, right: UnitId): String

  def link(left: UnitId, right: UnitId): String

  def dependencies(detangled: Detangled, context: UnitId, unit: UnitId): Seq[UnitId]
}

object ReasonDirection {
  private val valuesBuffer = new ArrayBuffer[ReasonDirection]
  lazy val values = valuesBuffer.toSeq
  val TowardDependsOn = new ReasonDirection("depends on") {
    override def name(left: UnitId, right: UnitId): String = HtmlUtil.reasonName(left, right)

    override def link(left: UnitId, right: UnitId): String = HtmlUtil.reasonLink(left, right)

    override def dependencies(detangled: Detangled, context: UnitId, unit: UnitId): Seq[UnitId] = detangled.dependsOn(context, unit)
  }
  val TowardDependedOnBy = new ReasonDirection("depended on by") {
    override def name(left: UnitId, right: UnitId): String = HtmlUtil.reasonName(right, left)

    override def link(left: UnitId, right: UnitId): String = HtmlUtil.reasonLink(right, left)

    override def dependencies(detangled: Detangled, context: UnitId, unit: UnitId): Seq[UnitId] = detangled.dependedOnBy(context, unit)
  }
}
