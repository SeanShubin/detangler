package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}
import org.scalatest.FunSuite

class DetanglerTest extends FunSuite {
  test("moderately complex analysis") {
    val dependencies = Seq(
      ("a", "b"),
      ("b", "c"),
      ("b", "d"),
      ("d", "e"),
      ("e", "f"),
      ("e", "g"),
      ("f", "d"),
      ("g", "h"),
      ("h", "g"),
      ("h", "i"),
      ("i", "j")
    )
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
    val cycleDEF = Cycle(Set(d, e, f))
    val cycleGH = Cycle(Set(g, h))
    val expectedModules = Seq(a, b, cycleDEF, d, e, f, cycleGH, g, h, i, c, j)

    val cycleFinder = new CycleFinderWarshall
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)

    assert(detangled.levelsDeep === 1)
    assert(detangled.childModules(detangled.root()) === expectedModules)
    checkStandalone(detangled, a, 9, 9, Seq(b), Seq())
    checkStandalone(detangled, b, 8, 8, Seq(), Seq())
    checkStandalone(detangled, c, 1, 0, Seq(), Seq())
    checkStandalone(detangled, d, 7, 4, Seq(), Seq())
    checkStandalone(detangled, e, 7, 4, Seq(), Seq())
    checkStandalone(detangled, f, 7, 4, Seq(), Seq())
    checkStandalone(detangled, g, 4, 2, Seq(), Seq())
    checkStandalone(detangled, h, 4, 2, Seq(), Seq())
    checkStandalone(detangled, i, 2, 1, Seq(), Seq())
    checkStandalone(detangled, j, 1, 0, Seq(), Seq())
    checkCycle(detangled, cycleDEF, 7, 4, Seq(d, e, f), Seq(), Seq())
    checkCycle(detangled, cycleGH, 4, 2, Seq(g, h), Seq(), Seq())
  }

  def checkStandalone(detangled: Detangled,
                      standalone: Standalone,
                      depth: Int,
                      complexity: Int,
                      dependsOn: Seq[Standalone],
                      dependedOnBy: Seq[Standalone]): Unit = {
    assert(detangled.depth(standalone) === depth, standalone)
    assert(detangled.complexity(standalone) === complexity, standalone)
    assert(detangled.dependsOn(standalone) === dependsOn, standalone)
    assert(detangled.dependedOnBy(standalone) === dependedOnBy, standalone)
  }

  def checkCycle(detangled: Detangled,
                 cycle: Cycle,
                 depth: Int,
                 complexity: Int,
                 parts: Seq[Standalone],
                 dependsOn: Seq[Standalone],
                 dependedOnBy: Seq[Standalone]): Unit = {
    assert(detangled.depth(cycle) === depth, cycle)
    assert(detangled.complexity(cycle) === complexity, cycle)
    assert(detangled.cycleParts(cycle) === parts, cycle)
    assert(detangled.dependsOn(cycle) === dependsOn, cycle)
    assert(detangled.dependedOnBy(cycle) === dependedOnBy, cycle)
  }
}
