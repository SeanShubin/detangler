package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Single}

object SampleDataWithCycles {
  val moduleRoot = Single(Seq())
  val moduleA = Single(Seq("group/a"))
  val moduleB = Single(Seq("group/b"))
  val moduleC = Single(Seq("group/a", "package/c"))
  val moduleD = Single(Seq("group/a", "package/d"))
  val moduleE = Single(Seq("group/b", "package/e"))
  val moduleF = Single(Seq("group/a", "package/c", "class/f"))
  val moduleG = Single(Seq("group/a", "package/c", "class/g"))
  val moduleH = Single(Seq("group/a", "package/d", "class/h"))
  val moduleI = Single(Seq("group/b", "package/e", "class/i"))
  val cycleAB = Cycle(Set(moduleA, moduleB))
  val cycleCD = Cycle(Set(moduleA, moduleB))
  val cycleFG = Cycle(Set(moduleA, moduleB))
  private val map: Map[Module, ModuleInfo] = Map(
    moduleRoot -> ModuleInfo(
      id = moduleRoot,
      children = Set(moduleA, moduleB, cycleAB)
    ),
    moduleA -> ModuleInfo(
      id = moduleA,
      children = Set(moduleC, moduleD, cycleCD),
      dependsOn = Set(moduleB),
      dependedOnBy = Set(moduleB),
      depth = 1,
      complexity = 1
    ),
    moduleB -> ModuleInfo(
      id = moduleB,
      children = Set(moduleE),
      dependsOn = Set(moduleA),
      dependedOnBy = Set(moduleA)
    ),
    moduleC -> ModuleInfo(
      id = moduleC,
      children = Set(moduleF, moduleG, cycleFG),
      dependsOn = Set(moduleD),
      dependedOnBy = Set(moduleD),
      depth = 1,
      complexity = 1
    ),
    moduleD -> ModuleInfo(
      id = moduleD,
      children = Set(moduleH),
      dependsOn = Set(moduleC),
      dependedOnBy = Set(moduleC)
    ),
    moduleE -> ModuleInfo(
      id = moduleE,
      children = Set(moduleI)
    ),
    moduleF -> ModuleInfo(
      id = moduleF,
      dependsOn = Set(moduleG),
      dependedOnBy = Set(moduleG)
    ),
    moduleG -> ModuleInfo(
      id = moduleG,
      dependsOn = Set(moduleF),
      dependedOnBy = Set(moduleF)
    ),
    moduleH -> ModuleInfo(
      id = moduleH
    ),
    moduleI -> ModuleInfo(
      id = moduleI
    ),
    cycleAB -> ModuleInfo(
      id = cycleAB,
      parts = Set(moduleA, moduleB)
    ),
    cycleCD -> ModuleInfo(
      id = cycleCD,
      parts = Set(moduleC, moduleD)
    ),
    cycleFG -> ModuleInfo(
      id = cycleFG,
      parts = Set(moduleF, moduleG)
    )
  )
  val detangled = new Detangled {
    override def root(): Single = moduleRoot

    override def children(single: Single): Set[Module] = map(single).children
  }
}
