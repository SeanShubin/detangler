package com.seanshubin.detangler.collection

import org.scalatest.FunSuite

class MapUtilitiesTest extends FunSuite {
  test("map function over map of sequence") {
    val original = Map(1 -> Seq(2, 3), 4 -> Seq(5, 6))
    val expected = Map("11" -> Seq("22", "33"), "44" -> Seq("55", "66"))
    def f(x: Int): String = x.toString * 2
    val actual = MapUtilities.map(original, f)
    assert(actual === expected)
  }

  test("merge map of sequence") {
    val a = Map(1 -> Set(2, 3), 4 -> Set(5, 6))
    val b = Map(1 -> Set(3, 4), 2 -> Set(5, 6))
    val expected = Map(1 -> Set(2, 3, 4), 2 -> Set(5, 6), 4 -> Set(5, 6))
    val actual = MapUtilities.merge(a, b)
    assert(actual === expected)
  }

  test("map function over values in map of sequence") {
    val original = Map(1 -> Seq(2, 3), 4 -> Seq(5, 6))
    val expected = Map(1 -> Set(2, 3), 4 -> Set(5, 6))
    val actual = original.mapValues(_.toSet)
    assert(actual === expected)
  }
}
