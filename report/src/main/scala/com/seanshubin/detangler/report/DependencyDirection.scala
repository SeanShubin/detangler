package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Module, Standalone}

import scala.collection.mutable.ArrayBuffer

sealed abstract case class DependencyDirection(caption: String) {
  DependencyDirection.valuesBuffer.append(this)

  def dependenciesFor(detangled: Detangled, module: Module): Seq[Standalone]

  def name(left: Standalone, right: Standalone): String

  def link(left: Standalone, right: Standalone): String
}

object DependencyDirection {
  lazy val values: Seq[DependencyDirection] = valuesBuffer.toSeq
  private val valuesBuffer = new ArrayBuffer[DependencyDirection]()
  val TowardDependsOn = new DependencyDirection("depends on") {
    override def dependenciesFor(detangled: Detangled, module: Module): Seq[Standalone] = {
      detangled.dependsOn(module)
    }

    override def name(left: Standalone, right: Standalone): String = HtmlRender.reasonLinkName(left, right)

    override def link(left: Standalone, right: Standalone): String = HtmlRender.reasonLink(left, right)

  }
  val TowardDependedOnBy = new DependencyDirection("depended on by") {
    override def dependenciesFor(detangled: Detangled, module: Module): Seq[Standalone] = {
      detangled.dependedOnBy(module)
    }

    override def name(left: Standalone, right: Standalone): String = HtmlRender.reasonLinkName(right, left)

    override def link(left: Standalone, right: Standalone): String = HtmlRender.reasonLink(right, left)
  }
}
