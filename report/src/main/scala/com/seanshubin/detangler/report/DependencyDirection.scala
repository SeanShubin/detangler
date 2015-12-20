package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

import scala.collection.mutable.ArrayBuffer

sealed abstract case class DependencyDirection(caption: String) {
  DependencyDirection.valuesBuffer.append(this)

  def dependenciesFor(detangled: Detangled, standalone: Standalone): Set[Standalone]

  def name(left: Standalone, right: Standalone): String

  def link(left: Standalone, right: Standalone): String
}

object DependencyDirection {
  private val valuesBuffer = new ArrayBuffer[DependencyDirection]()
  lazy val values: Seq[DependencyDirection] = valuesBuffer.toSeq
  val TowardDependsOn = new DependencyDirection("depends on") {
    override def dependenciesFor(detangled: Detangled, standalone: Standalone): Set[Standalone] = {
      detangled.dependsOn(standalone)
    }

    override def name(left: Standalone, right: Standalone): String = HtmlRendering.reasonName(left, right)

    override def link(left: Standalone, right: Standalone): String = HtmlRendering.reasonLink(left, right)

  }
  val TowardDependedOnBy = new DependencyDirection("depended on by") {
    override def dependenciesFor(detangled: Detangled, standalone: Standalone): Set[Standalone] = {
      detangled.dependedOnBy(standalone)
    }

    override def name(left: Standalone, right: Standalone): String = HtmlRendering.reasonName(right, left)

    override def link(left: Standalone, right: Standalone): String = HtmlRendering.reasonLink(right, left)
  }
}
