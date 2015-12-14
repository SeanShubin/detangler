package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Single}

import scala.collection.parallel.immutable.ParSet

object SampleDataWithCycles {
  val moduleRoot = Single(Seq(), 0, 0)
  val moduleA = Single(Seq("group/a"), 1, 2)
  val moduleB = Single(Seq("group/b"), 3, 4)
  val moduleC = Single(Seq("group/a", "package/c"), 5, 6)
  val moduleD = Single(Seq("group/a", "package/d"), 7, 8)
  val moduleE = Single(Seq("group/b", "package/e"), 9, 10)
  val moduleF = Single(Seq("group/a", "package/c", "class/f"), 11, 12)
  val moduleG = Single(Seq("group/a", "package/c", "class/g"), 13, 14)
  val moduleH = Single(Seq("group/a", "package/d", "class/h"), 15, 16)
  val moduleI = Single(Seq("group/b", "package/e", "class/i"), 17, 18)
  val cycleAB = Cycle(Set(moduleA, moduleB), 19, 20)
  val cycleCD = Cycle(Set(moduleA, moduleB), 21, 22)
  val cycleFG = Cycle(Set(moduleA, moduleB), 23, 24)
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
