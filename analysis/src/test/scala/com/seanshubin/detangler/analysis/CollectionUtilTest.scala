package com.seanshubin.detangler.analysis

import org.scalatest.FunSuite

import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

class CollectionUtilTest extends FunSuite {
  test("convert pairs to map of set") {
    val data = Seq((1, 2), (3, 4), (3, 5), (3, 6))
    val expected = Map(1 -> Set(2), 3 -> Set(4, 5, 6))
    val actual = data.foldLeft(Map[Int, Set[Int]]())(CollectionUtil.appendPairToMapFromKeyToSetOfValues)
    assert(actual === expected)
  }

  test("map over keys and values in map of set") {
    val data = Map(1 -> Set(2), 3 -> Set(4, 5, 6))
    val function: (Int => Int) = (x) => x * 2
    val expected = Map(2 -> Set(4), 6 -> Set(8, 10, 12))
    val actual = data.map(CollectionUtil.functionOverPairWithKeyAndSetOfValues(function))
    assert(actual === expected)
  }

  test("mutable to immutable with map of key to set of values") {
    val mutable: MutableMap[Int, MutableSet[Int]] = MutableMap(1 -> MutableSet(2), 3 -> MutableSet(4, 5, 6))
    val expected: Map[Int, Set[Int]] = Map(1 -> Set(2), 3 -> Set(4, 5, 6))
    val actual = CollectionUtil.mutableToImmutable(mutable)
    assert(actual === expected)
  }

  test("invert map of set") {
    val data = Map(1 -> Set(2, 3), 3 -> Set(2, 4))
    val expected = Map(2 -> Set(1, 3), 3 -> Set(1), 4 -> Set(3))
    val actual = CollectionUtil.invert(data)
    assert(actual === expected)
  }
}
