package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}
import org.scalatest.FunSuite

class DetanglerTest extends FunSuite {
  test("moderately complex analysis") {
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
    val cycleDEF = Cycle(Set(d, e, f))
    val cycleGH = Cycle(Set(g, h))
    val expectedModules = Set(a, b, cycleDEF, d, e, f, cycleGH, g, h, i, c, j)

    val cycleFinder = new CycleFinderWarshall[Standalone]
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)

    assert(detangled.levelsDeep === 1)
    assert(detangled.childModules(detangled.root()) === expectedModules)
    checkStandalone(detangled, a, 1, 8, 9, Set(b), Set())
    checkStandalone(detangled, b, 2, 7, 8, Set(c, d), Set(a))
    checkStandalone(detangled, c, 0, 0, 0, Set(), Set(b))
    checkStandalone(detangled, d, 1, 6, 6, Set(e), Set(b, f))
    checkStandalone(detangled, e, 2, 6, 6, Set(f, g), Set(d))
    checkStandalone(detangled, f, 1, 6, 6, Set(d), Set(e))
    checkStandalone(detangled, g, 1, 3, 3, Set(h), Set(e, h))
    checkStandalone(detangled, h, 2, 3, 3, Set(g, i), Set(g))
    checkStandalone(detangled, i, 1, 1, 1, Set(j), Set(h))
    checkStandalone(detangled, j, 0, 0, 0, Set(), Set(i))
    checkCycle(detangled, cycleDEF, 1, 6, 4, Set(d, e, f), Set(g), Set(b))
    checkCycle(detangled, cycleGH, 1, 3, 2, Set(g, h), Set(i), Set(e))
  }

  def checkStandalone(detangled: Detangled,
                      standalone: Standalone,
                      breadth: Int,
                      depth: Int,
                      transitive: Int,
                      dependsOn: Set[Standalone],
                      dependedOnBy: Set[Standalone]): Unit = {
    assert(detangled.breadth(standalone) === breadth, s"breadth for $standalone")
    assert(detangled.depth(standalone) === depth, s"depth for $standalone")
    assert(detangled.transitive(standalone) === transitive, s"transitive for $standalone")
    assert(detangled.dependsOn(standalone) === dependsOn, s"dependsOn for $standalone")
    assert(detangled.dependedOnBy(standalone) === dependedOnBy, s"dependedOnBy for $standalone")
  }

  def checkCycle(detangled: Detangled,
                 cycle: Cycle,
                 breadth: Int,
                 depth: Int,
                 transitive: Int,
                 parts: Set[Standalone],
                 dependsOn: Set[Standalone],
                 dependedOnBy: Set[Standalone]): Unit = {
    assert(detangled.breadth(cycle) === breadth, s"breadth for $cycle")
    assert(detangled.depth(cycle) === depth, s"depth for $cycle")
    assert(detangled.transitive(cycle) === transitive, s"transitive for $cycle")
    assert(detangled.cycleParts(cycle) === parts, s"cycleParts for $cycle")
    assert(detangled.dependsOn(cycle) === dependsOn, s"dependsOn for $cycle")
    assert(detangled.dependedOnBy(cycle) === dependedOnBy, s"dependedOnBy for $cycle")
  }
}
