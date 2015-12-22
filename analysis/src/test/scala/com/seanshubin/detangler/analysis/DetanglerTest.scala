package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Standalone}
import org.scalatest.FunSuite

class DetanglerTest extends FunSuite {
  test("moderately complex analysis") {
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
    val cycleDEF = Cycle(Set(d, e, f))
    val cycleGH = Cycle(Set(g, h))
    val expectedModules: Set[Module] = Set(a, b, cycleDEF, d, e, f, cycleGH, g, h, i, c, j)

    val cycleFinder = new CycleFinderWarshall[Standalone]
    val detangler = new DetanglerImpl(cycleFinder)
    val detangled = detangler.analyze(dependencies)

    assert(detangled.levelsDeep === 1)
    //    checkStandalone(detangled, root, 0, 0, 0, Set(), Set(), expectedModules)
    checkStandalone(detangled, a, 1, 8, 9, Set(b), Set(), Set())
    checkStandalone(detangled, b, 2, 7, 8, Set(c, d), Set(a), Set())
    checkStandalone(detangled, c, 0, 0, 0, Set(), Set(b), Set())
    checkStandalone(detangled, d, 1, 6, 6, Set(e), Set(b, f), Set())
    checkStandalone(detangled, e, 2, 6, 6, Set(f, g), Set(d), Set())
    checkStandalone(detangled, f, 1, 6, 6, Set(d), Set(e), Set())
    checkStandalone(detangled, g, 1, 3, 3, Set(h), Set(e, h), Set())
    checkStandalone(detangled, h, 2, 3, 3, Set(g, i), Set(g), Set())
    checkStandalone(detangled, i, 1, 1, 1, Set(j), Set(h), Set())
    checkStandalone(detangled, j, 0, 0, 0, Set(), Set(i), Set())
    checkCycle(detangled, cycleDEF, 1, 6, 4, Set(d, e, f), Set(g), Set(b))
    checkCycle(detangled, cycleGH, 1, 3, 2, Set(g, h), Set(i), Set(e))
  }

  test("multiple levels deep") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val groupB = Standalone(Seq("group/b"))
    val packageC = Standalone(Seq("group/a", "package/c"))
    val packageD = Standalone(Seq("group/a", "package/d"))
    val packageE = Standalone(Seq("group/b", "package/e"))
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

    assert(detangled.levelsDeep === 3)

    //    checkStandalone(detangled, root, 0, 0, 0, Set(), Set(), Set(groupA, groupB))
    checkStandalone(detangled, groupA, 1, 1, 1, Set(groupB), Set(), Set(packageC, packageD))
    checkStandalone(detangled, groupB, 0, 0, 0, Set(), Set(groupA), Set(packageE))
    checkStandalone(detangled, packageC, 2, 1, 2, Set(packageD), Set(), Set(classF, classG))
    checkStandalone(detangled, packageD, 0, 0, 0, Set(), Set(packageC), Set(classH))
    checkStandalone(detangled, packageE, 0, 0, 0, Set(), Set(packageC), Set(classI))
    checkStandalone(detangled, classF, 1, 3, 3, Set(classG, classH, classI), Set(), Set())
    checkStandalone(detangled, classG, 0, 0, 0, Set(), Set(classF), Set())
    checkStandalone(detangled, classH, 0, 0, 0, Set(), Set(classF), Set())
    checkStandalone(detangled, classI, 0, 0, 0, Set(), Set(classF), Set())
  }

  test("multiple levels deep with cycles") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val groupB = Standalone(Seq("group/b"))
    val packageC = Standalone(Seq("group/a", "package/c"))
    val packageD = Standalone(Seq("group/a", "package/d"))
    val packageE = Standalone(Seq("group/b", "package/e"))
    val classF = Standalone(Seq("group/a", "package/c", "class/f"))
    val classG = Standalone(Seq("group/a", "package/c", "class/g"))
    val classH = Standalone(Seq("group/a", "package/d", "class/h"))
    val classI = Standalone(Seq("group/b", "package/e", "class/i"))
    val groupAB = Cycle(Set(groupA, groupB))
    val packageCD = Cycle(Set(packageC, packageD))
    val cycleFG = Cycle(Set(classF, classG))

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

    assert(detangled.levelsDeep === 3)

    checkStandalone(detangled, root, 0, 0, 0, Set(), Set(), Set(groupA, groupB, groupAB))
    checkStandalone(detangled, groupA, 1, 2, 1, Set(groupB), Set(groupB), Set(packageC, packageD))
    checkStandalone(detangled, groupB, 1, 2, 1, Set(groupA), Set(groupA), Set(packageE))
    checkStandalone(detangled, packageC, 1, 2, 1, Set(packageD, packageE), Set(), Set(classF, classG))
    checkStandalone(detangled, packageD, 1, 2, 1, Set(packageC), Set(packageC), Set(classH))
    checkStandalone(detangled, packageE, 0, 0, 0, Set(), Set(packageC), Set(classI))
    checkStandalone(detangled, classF, 1, 3, 3, Set(classG), Set(classG), Set())
    checkStandalone(detangled, classG, 0, 0, 0, Set(classF), Set(classF), Set())
    checkStandalone(detangled, classH, 0, 0, 0, Set(), Set(), Set())
    checkStandalone(detangled, classI, 0, 0, 0, Set(), Set(), Set())

    checkCycle(detangled, groupAB, 0, 0, 0, Set(groupA, groupB), Set(), Set())
    checkCycle(detangled, packageCD, 0, 0, 0, Set(packageC, packageD), Set(), Set())
    checkCycle(detangled, cycleFG, 0, 0, 0, Set(classF, classG), Set(), Set())
  }

  def checkStandalone(detangled: Detangled,
                      standalone: Standalone,
                      breadth: Int,
                      depth: Int,
                      transitive: Int,
                      dependsOn: Set[Standalone],
                      dependedOnBy: Set[Standalone],
                      children: Set[Module]): Unit = {
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
