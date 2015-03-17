package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class UnitIdTest extends FunSuite {
  test("sort by name") {
    val unitIds: Seq[UnitId] = Seq(UnitId.simple("b"), UnitId.simple("d"), UnitId.simple("c"), UnitId.simple("a"))
    val expected = Seq(UnitId.simple("a"), UnitId.simple("b"), UnitId.simple("c"), UnitId.simple("d"))
    val actual = unitIds.sorted
    assert(actual === expected)
  }

  test("sort by path elements in order") {
    val unitIds: Seq[UnitId] = Seq(UnitId.complex(Set("a", "d")), UnitId.complex(Set("b", "c")), UnitId.complex(Set("b", "d")), UnitId.complex(Set("a", "c")))
    val expected = Seq(UnitId.complex(Set("a", "c")), UnitId.complex(Set("a", "d")), UnitId.complex(Set("b", "c")), UnitId.complex(Set("b", "d")))
    val actual = unitIds.sorted
    assert(actual === expected)
  }

  test("id string") {
    val unitId = UnitId.complex(Set("a", "b"), Set("c"), Set("d", "e", "f"))
    assert(unitId.idAsString === "a-b--c--d-e-f")
  }
}
