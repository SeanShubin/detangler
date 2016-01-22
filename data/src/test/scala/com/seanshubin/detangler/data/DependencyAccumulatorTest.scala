package com.seanshubin.detangler.data

import org.scalatest.FunSuite

class DependencyAccumulatorTest extends FunSuite {
  val classF = Seq("group/a", "package/c", "class/f")
  val classG = Seq("group/a", "package/c", "class/g")
  val classH = Seq("group/a", "package/d", "class/h")
  val classI = Seq("group/b", "package/e", "class/i")

  test("empty") {
    val accumulator = DependencyAccumulator.empty[Seq[String]]()
    assert(accumulator.dependencies === Map())
  }

  test("add one item") {
    val accumulator = DependencyAccumulator.empty[Seq[String]]().addValues(classF, Set(classG))
    assert(accumulator.dependencies === Map(classF -> Set(classG), classG -> Set()))
  }

  test("do not depend on self") {
    val accumulator = DependencyAccumulator.empty[Seq[String]]().addValues(classF, Set(classF))
    assert(accumulator.dependencies === Map(classF -> Set()))
  }

  test("remove duplicates") {
    val accumulator = DependencyAccumulator.empty[Seq[String]]().addValues(classF, Set(classG, classG)).addValues(classF, Set(classG, classG))
    assert(accumulator.dependencies === Map(classF -> Set(classG), classG -> Set()))
  }

  test("add item without dependencies") {
    val collector = DependencyAccumulator.empty[Seq[String]]().addValues(classF, Set())
    assert(collector.dependencies === Map(classF -> Set()))
  }

  test("from iterator") {
    val rawDataSeq: Seq[(Seq[String], Set[Seq[String]])] = Seq(
      classF -> Set(classG),
      classF -> Set(classH),
      classF -> Set(classI)
    )
    val accumulator = DependencyAccumulator.fromIterable(rawDataSeq.map(Seq(_).toMap))
    assert(accumulator.dependencies === Map(
      classF -> Set(classG, classH, classI),
      classG -> Set(),
      classH -> Set(),
      classI -> Set()))
  }

  test("transpose") {
    val accumulator = DependencyAccumulator[Seq[String]](Map(
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
