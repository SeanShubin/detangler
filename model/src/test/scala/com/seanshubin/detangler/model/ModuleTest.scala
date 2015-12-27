package com.seanshubin.detangler.model

import org.scalatest.FunSuite

class ModuleTest extends FunSuite {
  val standaloneA = Standalone(Seq("a", "b", "c"))
  val standaloneB = Standalone(Seq("a", "b"))
  val standaloneC = Standalone(Seq("a", "b", "d"))
  val standaloneD = Standalone(Seq("a", "d", "c"))
  val standaloneE = Standalone(Seq("d", "b", "c"))
  val standaloneF = Standalone(Seq())
  val standaloneG = Standalone(Seq("a", "b", "e"))
  val cycleA = Cycle(Set(standaloneA, standaloneC))
  val cycleB = Cycle(Set(standaloneA, standaloneG))

  test("compare modules") {
    assert(Module.compare(standaloneA, standaloneA) === 0)
    assert(Module.compare(standaloneA, standaloneB) > 0)
    assert(Module.compare(standaloneA, standaloneC) < 0)
    assert(Module.compare(standaloneA, standaloneD) < 0)
    assert(Module.compare(standaloneA, standaloneE) < 0)
    assert(Module.compare(standaloneA, standaloneF) > 0)
    assert(Module.compare(standaloneA, cycleA) > 0)
    assert(Module.compare(standaloneB, standaloneA) < 0)
    assert(Module.compare(standaloneC, standaloneA) > 0)
    assert(Module.compare(standaloneD, standaloneA) > 0)
    assert(Module.compare(standaloneE, standaloneA) > 0)
    assert(Module.compare(standaloneF, standaloneA) < 0)
    assert(Module.compare(cycleA, standaloneA) < 0)
    assert(Module.compare(cycleA, cycleA) === 0)
    assert(Module.compare(cycleA, cycleB) < 0)
    assert(Module.compare(cycleB, cycleA) > 0)
  }
}
