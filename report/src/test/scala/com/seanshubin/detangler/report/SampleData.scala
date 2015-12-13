package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Module, Single}

import scala.collection.parallel.immutable.ParSet

object SampleData {
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
  private val map: Map[Module, ModuleInfo] = Map(
    moduleRoot -> ModuleInfo(module = moduleRoot, children = ParSet(moduleA, moduleB)),
    moduleA -> ModuleInfo(module = moduleA, children = ParSet(moduleC, moduleD)),
    moduleB -> ModuleInfo(module = moduleB, children = ParSet(moduleE)),
    moduleC -> ModuleInfo(module = moduleC, children = ParSet(moduleF, moduleG)),
    moduleD -> ModuleInfo(module = moduleD, children = ParSet(moduleH)),
    moduleE -> ModuleInfo(module = moduleE, children = ParSet(moduleI)),
    moduleF -> ModuleInfo(module = moduleF, children = ParSet()),
    moduleG -> ModuleInfo(module = moduleG, children = ParSet()),
    moduleH -> ModuleInfo(module = moduleH, children = ParSet()),
    moduleI -> ModuleInfo(module = moduleI, children = ParSet())
  )
  val detangled = new Detangled {
    override def root(): Single = moduleRoot

    override def children(single: Single): ParSet[Module] = map(single).children
  }

  private case class ModuleInfo(module: Module, children: ParSet[Module])

}
