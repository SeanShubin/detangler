package com.seanshubin.detangler.collection

import org.scalatest.FunSuite

class MapOverKeysAndValuesTest extends FunSuite {
  test("map of sequence"){
    val original = Map(1 -> Seq(2,3), 4 -> Seq(5,6))
    val expected = Map("11" -> Seq("22", "33"), "44" -> Seq("55", "66"))
    def f(x:Int):String = x.toString * 2
    val actual = MapOverKeysAndValues.map(original, f)
    assert(actual === expected)
  }
}
