package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Module, Single}

import scala.collection.parallel.immutable.ParSet

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
