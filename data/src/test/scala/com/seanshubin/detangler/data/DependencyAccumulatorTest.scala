package com.seanshubin.detangler.data

import org.scalatest.FunSuite

class DependencyAccumulatorTest extends FunSuite {
  val classF = Seq("group/a", "package/c", "class/f")
  val classG = Seq("group/a", "package/c", "class/g")
  val classH = Seq("group/a", "package/d", "class/h")
  val classI = Seq("group/b", "package/e", "class/i")

  test("empty") {
    val accumulator = DependencyAccumulator.Empty
    assert(accumulator.dependencies === Map())
  }

  test("add one item") {
    val accumulator = DependencyAccumulator.Empty.addValues(classF, Seq(classG))
    assert(accumulator.dependencies === Map(classF -> Set(classG), classG -> Set()))
  }

  test("do not depend on self") {
    val accumulator = DependencyAccumulator.Empty.addValues(classF, Seq(classF))
    assert(accumulator.dependencies === Map(classF -> Set()))
  }

  test("remove duplicates") {
    val accumulator = DependencyAccumulator.Empty.addValues(classF, Seq(classG, classG)).addValues(classF, Seq(classG, classG))
    assert(accumulator.dependencies === Map(classF -> Set(classG), classG -> Set()))
  }

  test("add item without dependencies") {
    val collector = DependencyAccumulator.Empty.addValues(classF, Seq())
    assert(collector.dependencies === Map(classF -> Set()))
  }

  test("from iterator") {
    val rawDataSeq: Seq[(Seq[String], Seq[Seq[String]])] = Seq(
      classF -> Seq(classG),
      classF -> Seq(classH),
      classF -> Seq(classI)
    )
    val accumulator = DependencyAccumulator.fromIterator(rawDataSeq.iterator)
    assert(accumulator.dependencies === Map(
      classF -> Set(classG, classH, classI),
      classG -> Set(),
      classH -> Set(),
      classI -> Set()))
  }

  test("transpose") {
    val accumulator = DependencyAccumulator(Map(
      classF -> Set(classG, classH),
      classG -> Set(classH, classI),
      classH -> Set(),
      classI -> Set()))
    val actual = accumulator.transpose().dependencies
    val expected = Map(
      classF -> Set(),
      classG -> Set(classF),
      classH -> Set(classF, classG),
      classI -> Set(classG))
    assert(actual === expected)
  }
}
