package com.seanshubin.detangler.core

import com.seanshubin.detangler.analysis.{CycleFinderWarshall, DetanglerImpl}
import com.seanshubin.detangler.model.{Detangled, Standalone}

object DetangledFactory {
  def sampleWithoutCycles(): Detangled = {
    val classF = Standalone(Seq("group/a", "package/c", "class/f"))
    val classG = Standalone(Seq("group/a", "package/c", "class/g"))
    val classH = Standalone(Seq("group/a", "package/d", "class/h"))
    val classI = Standalone(Seq("group/b", "package/e", "class/i"))

    val dependencies = Seq(
      (classF, classG),
      (classF, classH),
      (classF, classI)
    )
    val cycleFinder = new CycleFinderWarshall[Standalone]
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)
    detangled
  }

  def sampleWithCycles(): Detangled = {
    val classF = Standalone(Seq("group/a", "package/c", "class/f"))
    val classG = Standalone(Seq("group/a", "package/c", "class/g"))
    val classH = Standalone(Seq("group/a", "package/d", "class/h"))
    val classI = Standalone(Seq("group/b", "package/e", "class/i"))

    val dependencies = Seq(
      (classF, classG),
      (classF, classH),
      (classF, classI),
      (classG, classF),
      (classH, classF),
      (classI, classF)
    )
    val cycleFinder = new CycleFinderWarshall[Standalone]
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)
    detangled
  }
}
