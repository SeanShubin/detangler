package com.seanshubin.detangler.compare

import org.scalatest.FunSuite

class CompareTest extends FunSuite {
  val samples = Seq(
    Sample("a", 1),
    Sample("a", 2),
    Sample("a", 3),
    Sample("b", 1),
    Sample("b", 2),
    Sample("b", 3),
    Sample("c", 1),
    Sample("c", 2),
    Sample("c", 3)
  )

  def nameAsc(left: Sample, right: Sample): Int = {
    left.name.compareTo(right.name)
  }

  def numberAsc(left: Sample, right: Sample): Int = {
    left.number.compareTo(right.number)
  }

  test("sort by name asc number asc") {
    val lessThan = Compare.lessThan(nameAsc, numberAsc)
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("a", 1),
        Sample("a", 2),
        Sample("a", 3),
        Sample("b", 1),
        Sample("b", 2),
        Sample("b", 3),
        Sample("c", 1),
        Sample("c", 2),
        Sample("c", 3)))
  }

  test("sort by name desc number asc") {
    val lessThan = Compare.lessThan(Compare.reverse(nameAsc), numberAsc)
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("c", 1),
        Sample("c", 2),
        Sample("c", 3),
        Sample("b", 1),
        Sample("b", 2),
        Sample("b", 3),
        Sample("a", 1),
        Sample("a", 2),
        Sample("a", 3)))
  }

  test("sort by name asc number desc") {
    val lessThan = Compare.lessThan(nameAsc, Compare.reverse(numberAsc))
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("a", 3),
        Sample("a", 2),
        Sample("a", 1),
        Sample("b", 3),
        Sample("b", 2),
        Sample("b", 1),
        Sample("c", 3),
        Sample("c", 2),
        Sample("c", 1)))
  }

  test("sort by name desc number desc") {
    val lessThan = Compare.lessThan(Compare.reverse(nameAsc), Compare.reverse(numberAsc))
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("c", 3),
        Sample("c", 2),
        Sample("c", 1),
        Sample("b", 3),
        Sample("b", 2),
        Sample("b", 1),
        Sample("a", 3),
        Sample("a", 2),
        Sample("a", 1)))
  }

  test("sort by number asc name asc") {
    val lessThan = Compare.lessThan(numberAsc, nameAsc)
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("a", 1),
        Sample("b", 1),
        Sample("c", 1),
        Sample("a", 2),
        Sample("b", 2),
        Sample("c", 2),
        Sample("a", 3),
        Sample("b", 3),
        Sample("c", 3)
      ))
  }

  test("sort by number asc name desc") {
    val lessThan = Compare.lessThan(numberAsc, Compare.reverse(nameAsc))
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("c", 1),
        Sample("b", 1),
        Sample("a", 1),
        Sample("c", 2),
        Sample("b", 2),
        Sample("a", 2),
        Sample("c", 3),
        Sample("b", 3),
        Sample("a", 3)
      ))
  }

  test("sort by number desc name asc") {
    val lessThan = Compare.lessThan(Compare.reverse(numberAsc), nameAsc)
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("a", 3),
        Sample("b", 3),
        Sample("c", 3),
        Sample("a", 2),
        Sample("b", 2),
        Sample("c", 2),
        Sample("a", 1),
        Sample("b", 1),
        Sample("c", 1)
      ))
  }

  test("sort by number desc name desc") {
    val lessThan = Compare.lessThan(Compare.reverse(numberAsc), Compare.reverse(nameAsc))
    assert(samples.sortWith(lessThan) ===
      Seq(
        Sample("c", 3),
        Sample("b", 3),
        Sample("a", 3),
        Sample("c", 2),
        Sample("b", 2),
        Sample("a", 2),
        Sample("c", 1),
        Sample("b", 1),
        Sample("a", 1)
      ))
  }

  test("compare lists") {
    val compareFunction = Compare.composeCompareSeqFunction(Ordering.String.compare)
    val a = Seq("aaa", "bbb")
    val b = Seq("aaa", "bbb", "ccc")
    val c = Seq("aaa", "ddd", "ccc")
    assert(compareFunction(a, a) === 0)
    assert(compareFunction(b, b) === 0)
    assert(compareFunction(c, c) === 0)
    assert(compareFunction(a, b) < 0)
    assert(compareFunction(a, c) < 0)
    assert(compareFunction(b, a) > 0)
    assert(compareFunction(b, c) < 0)
    assert(compareFunction(c, a) > 0)
    assert(compareFunction(c, b) > 0)
  }
}
