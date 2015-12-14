package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Module, Single}

object SampleData {
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
  private val map: Map[Module, ModuleInfo] = Map(
    moduleRoot -> ModuleInfo(
      id = moduleRoot,
      children = Set(moduleA, moduleB)
    ),
    moduleA -> ModuleInfo(
      id = moduleA,
      children = Set(moduleC, moduleD),
      dependsOn = Set(moduleB),
      depth = 1,
      complexity = 1
    ),
    moduleB -> ModuleInfo(
      id = moduleB,
      children = Set(moduleE),
      dependedOnBy = Set(moduleA)
    ),
    moduleC -> ModuleInfo(
      id = moduleC,
      children = Set(moduleF, moduleG),
      dependsOn = Set(moduleD),
      depth = 1,
      complexity = 1
    ),
    moduleD -> ModuleInfo(
      id = moduleD,
      children = Set(moduleH),
      dependedOnBy = Set(moduleC)
    ),
    moduleE -> ModuleInfo(
      id = moduleE,
      children = Set(moduleI)
    ),
    moduleF -> ModuleInfo(
      id = moduleF,
      dependsOn = Set(moduleG)
    ),
    moduleG -> ModuleInfo(
      id = moduleG,
      dependedOnBy = Set(moduleF)
    ),
    moduleH -> ModuleInfo(
      id = moduleH
    ),
    moduleI -> ModuleInfo(
      id = moduleI
    )
  )
  val detangled = new Detangled {
    override def root(): Single = moduleRoot

    override def children(single: Single): Set[Module] = map(single).children
  }
}
