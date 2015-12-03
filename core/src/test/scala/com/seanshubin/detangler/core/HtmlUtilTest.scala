package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class HtmlUtilTest extends FunSuite {
  test("file names") {
    assert(HtmlUtil.fileNameFor(SampleData.idRoot) === "index.html")
    assert(HtmlUtil.fileNameFor(SampleData.idGroupA) === "group_a.html")
    assert(HtmlUtil.fileNameFor(SampleData.idPackageC) === "group_a--package_c.html")
  }

  test("html id") {
    assert(HtmlUtil.htmlId(SampleData.idGroupA) === "group_a")
    assert(HtmlUtil.htmlId(SampleData.idPackageC) === "group_a--package_c")
    assert(HtmlUtil.htmlId(SampleData.idClassF) === "group_a--package_c--class_f")
  }

  test("html name") {
    assert(HtmlUtil.htmlName(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlName(SampleData.idPackageC) === "package/c")
    assert(HtmlUtil.htmlName(SampleData.idClassF) === "class/f")
  }

  test("html link") {
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idGroupA) === "#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idPackageC) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idClassF) === "group_a--package_c.html#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idPackageC) === "#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idClassF) === "group_a--package_c.html#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idPackageC) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idClassF) === "group_a--package_c.html#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idPackageC, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageC, SampleData.idPackageC) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idPackageC, SampleData.idClassF) === "#group_a--package_c--class_f")
    assert(HtmlUtil.htmlLink(SampleData.idPackageD, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageD, SampleData.idPackageC) === "group_a.html#group_a--package_c")
    assert(HtmlUtil.htmlLink(SampleData.idPackageD, SampleData.idClassF) === "group_a--package_c.html#group_a--package_c--class_f")
  }
}
