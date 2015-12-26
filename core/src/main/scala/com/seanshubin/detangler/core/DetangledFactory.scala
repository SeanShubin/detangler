package com.seanshubin.detangler.core

import java.util.Random

import com.seanshubin.detangler.analysis.{CycleFinderWarshall, DetanglerImpl}
import com.seanshubin.detangler.model.{Detangled, Standalone}

class DetangledFactory {
  val seed = 12345
  val random = new Random(seed)
  val bound = 'z' - 'a' + 1

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

  def generatedSampleData(): Detangled = {
    val modules = for {
      i <- 1 to 10
      groupName = randomName(12)
      j <- 1 to 10
      packageName = randomName(6)
      k <- 1 to 10
      className = randomName(3)
    } yield {
      Standalone(Seq(groupName, packageName, className))
    }
    def createDependencies(index: Int): Seq[(Standalone, Standalone)] = {
      for {
        i <- 1 to 10
      } yield {
        val from = modules(index)
        val toIndex = randomBetween(index, 1000)
        val to = modules(toIndex)
        (from, to)
      }
    }
    val dependencies = (0 until 1000).flatMap(createDependencies)
    val cycleFinder = new CycleFinderWarshall[Standalone]
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)
    detangled
  }

  def randomName(size: Int): String = {
    val firstLetter = randomChar().toUpper
    val remainingLetters = "" + Iterator.continually(randomChar).take(size).mkString
    "" + firstLetter + remainingLetters

  }

  def randomChar(): Char = {
    (random.nextInt(bound) + 'a').toChar
  }

  def randomBetween(from: Int, to: Int): Int = {
    random.nextInt(to - from) + from
  }
}
