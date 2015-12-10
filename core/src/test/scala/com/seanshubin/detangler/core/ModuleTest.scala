package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class ModuleTest extends FunSuite {
  test("sort by name") {
    val modules: Seq[Module] = Seq(Module.simple("b"), Module.simple("d"), Module.simple("c"), Module.simple("a"))
    val expected = Seq(Module.simple("a"), Module.simple("b"), Module.simple("c"), Module.simple("d"))
    val actual = modules.sorted
    assert(actual === expected)
  }

  test("sort by path elements in order") {
    val modules: Seq[Module] = Seq(Module.complex(Set("a", "d")), Module.complex(Set("b", "c")), Module.complex(Set("b", "d")), Module.complex(Set("a", "c")))
    val expected = Seq(Module.complex(Set("a", "c")), Module.complex(Set("a", "d")), Module.complex(Set("b", "c")), Module.complex(Set("b", "d")))
    val actual = modules.sorted
    assert(actual === expected)
  }

  test("id string") {
    val module = Module.complex(Set("a", "b"), Set("c"), Set("d", "e", "f"))
    assert(module.qualifiedName === "a-b--c--d-e-f")
  }
}
