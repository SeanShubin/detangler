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
    assert(Module.lessThan(standaloneA, standaloneA) === false)
    assert(Module.lessThan(standaloneA, standaloneB) === false)
    assert(Module.lessThan(standaloneA, standaloneC) === true)
    assert(Module.lessThan(standaloneA, standaloneD) === true)
    assert(Module.lessThan(standaloneA, standaloneE) === true)
    assert(Module.lessThan(standaloneA, standaloneF) === false)
    assert(Module.lessThan(standaloneA, cycleA) === false)
    assert(Module.lessThan(standaloneB, standaloneA) === true)
    assert(Module.lessThan(standaloneC, standaloneA) === false)
    assert(Module.lessThan(standaloneD, standaloneA) === false)
    assert(Module.lessThan(standaloneE, standaloneA) === false)
    assert(Module.lessThan(standaloneF, standaloneA) === true)
    assert(Module.lessThan(cycleA, standaloneA) === true)
    assert(Module.lessThan(cycleA, cycleA) === false)
    assert(Module.lessThan(cycleA, cycleB) === true)
    assert(Module.lessThan(cycleB, cycleA) === false)
  }
}
