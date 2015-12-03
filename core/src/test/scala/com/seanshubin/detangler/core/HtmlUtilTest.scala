package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class HtmlUtilTest extends FunSuite {
  test("file names") {
    assert(HtmlUtil.fileNameFor(SampleData.idRoot) === "index.html")
    assert(HtmlUtil.fileNameFor(SampleData.idGroupA) === "group_a.html")
    assert(HtmlUtil.fileNameFor(SampleData.idPackageA) === "group_a--package_c.html")
  }

  test("html id") {
    assert(HtmlUtil.htmlId(SampleData.idGroupA) === "group_a")
    assert(HtmlUtil.htmlId(SampleData.idPackageA) === "group_a--package_c")
    assert(HtmlUtil.htmlId(SampleData.idClassA) === "group_a--package_c--class_f")
  }

  test("html name") {
    assert(HtmlUtil.htmlName(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlName(SampleData.idPackageA) === "package/c")
    assert(HtmlUtil.htmlName(SampleData.idClassA) === "class/f")
  }

  test("html link") {
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idGroupA) === "#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idPackageA) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idClassA) === "group_a--package_c.html#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idPackageA) === "#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idClassA) === "group_a--package_c.html#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idPackageA) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idClassA) === "group_a--package_c.html#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idPackageA, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageA, SampleData.idPackageA) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idPackageA, SampleData.idClassA) === "#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idPackageB, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageB, SampleData.idPackageA) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idPackageB, SampleData.idClassA) === "group_a--package_c.html#group_a--package_c--class_f")
  }
}
