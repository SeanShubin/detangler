package com.seanshubin.detangler.core

import scala.collection.mutable.ArrayBuffer

sealed abstract case class ArrowDirection(caption: String) {
  ArrowDirection.valuesBuffer += this

  def arrowName(left: UnitId, right: UnitId): String

  def arrowLink(left: UnitId, right: UnitId): String

  def dependencies(detangled: Detangled, context:UnitId, unit: UnitId): Seq[UnitId]
}

object ArrowDirection {
  private val valuesBuffer = new ArrayBuffer[ArrowDirection]
  lazy val values = valuesBuffer.toSeq
  val TowardDependsOn = new ArrowDirection("depends on") {
    override def arrowName(left: UnitId, right: UnitId): String = HtmlUtil.arrowName(left, right)

    override def arrowLink(left: UnitId, right: UnitId): String = HtmlUtil.arrowLink(left, right)

    override def dependencies(detangled: Detangled, context:UnitId, unit: UnitId): Seq[UnitId] = detangled.dependsOn(context, unit)
  }
  val TowardDependedOnBy = new ArrowDirection("depended on by") {
    override def arrowName(left: UnitId, right: UnitId): String = HtmlUtil.arrowName(right, left)

    override def arrowLink(left: UnitId, right: UnitId): String = HtmlUtil.arrowLink(right, left)

    override def dependencies(detangled: Detangled, context:UnitId, unit: UnitId): Seq[UnitId] = detangled.dependedOnBy(context, unit)
  }
}
