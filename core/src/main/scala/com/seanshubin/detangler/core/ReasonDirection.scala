package com.seanshubin.detangler.core

import scala.collection.mutable.ArrayBuffer

sealed abstract case class ReasonDirection(caption: String) {
  ReasonDirection.valuesBuffer += this

  def name(left: Module, right: Module): String

  def link(left: Module, right: Module): String

  def dependencies(detangled: Detangled, context: Module, module: Module): Seq[Module]
}

object ReasonDirection {
  private val valuesBuffer = new ArrayBuffer[ReasonDirection]
  lazy val values = valuesBuffer.toSeq
  val TowardDependsOn = new ReasonDirection("depends on") {
    override def name(left: Module, right: Module): String = HtmlUtil.reasonName(left, right)

    override def link(left: Module, right: Module): String = HtmlUtil.reasonLink(left, right)

    override def dependencies(detangled: Detangled, context: Module, module: Module): Seq[Module] = detangled.dependsOn(context, module)
  }
  val TowardDependedOnBy = new ReasonDirection("depended on by") {
    override def name(left: Module, right: Module): String = HtmlUtil.reasonName(right, left)

    override def link(left: Module, right: Module): String = HtmlUtil.reasonLink(right, left)

    override def dependencies(detangled: Detangled, context: Module, module: Module): Seq[Module] = detangled.dependedOnBy(context, module)
  }
}
