package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Single}

import scala.collection.mutable.ArrayBuffer

sealed abstract case class DependencyDirection(caption: String) {
  DependencyDirection.valuesBuffer.append(this)

  def dependenciesFor(detangled: Detangled, single: Single): Set[Single]
}

object DependencyDirection {
  private val valuesBuffer = new ArrayBuffer[DependencyDirection]()
  lazy val values: Seq[DependencyDirection] = valuesBuffer.toSeq
  val TowardDependsOn = new DependencyDirection("depends on") {
    override def dependenciesFor(detangled: Detangled, single: Single): Set[Single] = {
      detangled.dependsOn(single)
    }
  }
  val TowardDependedOnBy = new DependencyDirection("depended on by") {
    override def dependenciesFor(detangled: Detangled, single: Single): Set[Single] = {
      detangled.dependsOn(single)
    }
  }
}
