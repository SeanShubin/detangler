package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Single}

import scala.collection.parallel.immutable.ParSet

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
    moduleRoot -> ModuleInfo(module = moduleRoot, children = ParSet(moduleA, moduleB, cycleAB), parts = ParSet()),
    moduleA -> ModuleInfo(module = moduleA, children = ParSet(moduleC, moduleD, cycleCD), parts = ParSet()),
    moduleB -> ModuleInfo(module = moduleB, children = ParSet(moduleE), parts = ParSet()),
    moduleC -> ModuleInfo(module = moduleC, children = ParSet(moduleF, moduleG, cycleFG), parts = ParSet()),
    moduleD -> ModuleInfo(module = moduleD, children = ParSet(moduleH), parts = ParSet()),
    moduleE -> ModuleInfo(module = moduleE, children = ParSet(moduleI), parts = ParSet()),
    moduleF -> ModuleInfo(module = moduleF, children = ParSet(), parts = ParSet()),
    moduleG -> ModuleInfo(module = moduleG, children = ParSet(), parts = ParSet()),
    moduleH -> ModuleInfo(module = moduleH, children = ParSet(), parts = ParSet()),
    moduleI -> ModuleInfo(module = moduleI, children = ParSet(), parts = ParSet()),
    cycleAB -> ModuleInfo(module = cycleAB, children = ParSet(), parts = ParSet(moduleA, moduleB)),
    cycleCD -> ModuleInfo(module = cycleCD, children = ParSet(), parts = ParSet(moduleC, moduleD)),
    cycleFG -> ModuleInfo(module = cycleFG, children = ParSet(), parts = ParSet(moduleF, moduleG))
  )
  val detangled = new Detangled {
    override def root(): Single = moduleRoot

    override def children(single: Single): ParSet[Module] = map(single).children
  }

  private case class ModuleInfo(module: Module, children: ParSet[Module], parts: ParSet[Single])

}
