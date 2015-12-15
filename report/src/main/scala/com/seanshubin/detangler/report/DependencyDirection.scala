package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Single}

import scala.collection.mutable.ArrayBuffer

sealed abstract case class DependencyDirection(caption: String) {
  DependencyDirection.valuesBuffer.append(this)

  def dependenciesFor(detangled: Detangled, context: Single, single: Single): Set[Single]

  def name(left: Single, right: Single): String

  def link(left: Single, right: Single): String
}

object DependencyDirection {
  private val valuesBuffer = new ArrayBuffer[DependencyDirection]()
  lazy val values: Seq[DependencyDirection] = valuesBuffer.toSeq
  val TowardDependsOn = new DependencyDirection("depends on") {
    override def dependenciesFor(detangled: Detangled, context: Single, single: Single): Set[Single] = {
      detangled.dependsOn(single)
    }

    override def name(left: Single, right: Single): String = HtmlUtil.reasonName(left, right)

    override def link(left: Single, right: Single): String = HtmlUtil.reasonLink(left, right)

  }
  val TowardDependedOnBy = new DependencyDirection("depended on by") {
    override def dependenciesFor(detangled: Detangled, context: Single, single: Single): Set[Single] = {
      detangled.dependedOnBy(single)
    }

    override def name(left: Single, right: Single): String = HtmlUtil.reasonName(right, left)

    override def link(left: Single, right: Single): String = HtmlUtil.reasonLink(right, left)
  }
}
