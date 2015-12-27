package com.seanshubin.detangler.core

import java.util.Random

import com.seanshubin.detangler.analysis.{CycleFinderWarshall, DetanglerImpl}
import com.seanshubin.detangler.model.{Detangled, Standalone}

object DetangledFactory {
  val seed = 12345
  val random = new Random(seed)
  val letterChoices: Seq[Char] = 'a' to 'z'
  val startSounds = Seq("b", "br", "c", "cl", "d", "f", "fl", "g", "gl", "j", "m", "n", "p", "pl", "qu", "r", "sh", "sk", "sp", "st", "str", "t", "th", "tr", "w", "wh", "wr")
  val middleSounds = Seq("a", "e", "ee", "ea", "i", "o", "u")
  val endSounds = Seq("b", "be", "ck", "de", "fe", "ke", "le", "lt", "me", "n", "p", "pe", "re", "t", "th", "te", "x")
  val startSyllables = for {
    startSound <- startSounds
    middleSound <- middleSounds
  } yield {
    startSound + middleSound
  }
  val middleSyllables = for {
    startSound <- startSounds
    middleSound <- middleSounds
    endSound <- endSounds
  } yield {
    startSound + middleSound + endSound
  }
  val endSyllables = for {
    middleSound <- middleSounds
    endSound <- endSounds
  } yield {
    middleSound + endSound
  }
  val allSyllables = startSyllables ++ middleSyllables ++ endSyllables

  def contrivedSample(): Detangled = {
    val root = Standalone(Seq())
    val a = Standalone(Seq("a"))
    val b = Standalone(Seq("b"))
    val c = Standalone(Seq("c"))
    val d = Standalone(Seq("d"))
    val e = Standalone(Seq("e"))
    val f = Standalone(Seq("f"))
    val g = Standalone(Seq("g"))
    val h = Standalone(Seq("h"))
    val i = Standalone(Seq("i"))
    val j = Standalone(Seq("j"))
    val dependencies = Seq(
      (a, b),
      (b, c),
      (b, d),
      (d, e),
      (e, f),
      (e, g),
      (f, d),
      (g, h),
      (h, g),
      (h, i),
      (i, j)
    )
    val cycleFinder = new CycleFinderWarshall[Standalone]
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)
    detangled
  }

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
      groupName = randomName(4)
      j <- 1 to 10
      packageName = randomName(3)
      k <- 1 to 10
      className = randomName(2)
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
    val syllables = Iterator.continually(randomSyllable).take(size).mkString
    capitalize(syllables)
  }

  def capitalize(s: String): String = {
    s.head.toUpper + s.tail
  }

  def randomSyllable: String = chooseRandom(allSyllables)

  def chooseRandom[T](choices: Seq[T]): T = choices(random.nextInt(choices.size))

  def randomBetween(from: Int, to: Int): Int = {
    random.nextInt(to - from) + from
  }
}
