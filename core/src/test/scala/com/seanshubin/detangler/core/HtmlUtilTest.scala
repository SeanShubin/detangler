package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class HtmlUtilTest extends FunSuite {
  test("file names") {
    assert(HtmlUtil.fileNameFor(SampleData.idRoot) === "index.html")
    assert(HtmlUtil.fileNameFor(SampleData.idGroupA) === "group_a.html")
    assert(HtmlUtil.fileNameFor(SampleData.idPackageA) === "group_a--package_a.html")
  }

  test("html id") {
    assert(HtmlUtil.htmlId(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlId(SampleData.idPackageA) === "group/a--package/a")
    assert(HtmlUtil.htmlId(SampleData.idClassA) === "group/a--package/a--class/a")
  }

  test("html name") {
    assert(HtmlUtil.htmlName(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlName(SampleData.idPackageA) === "package/a")
    assert(HtmlUtil.htmlName(SampleData.idClassA) === "class/a")
  }

  test("html link") {
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idGroupA) === "#group/a")
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idPackageA) === "group_a.html#group/a--package/a")
    assert(HtmlUtil.htmlLink(SampleData.idRoot, SampleData.idClassA) === "group_a--package_a.html#group/a--package/a--class/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idGroupA) === "index.html#group/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idPackageA) === "#group/a--package/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupA, SampleData.idClassA) === "group_a--package_a.html#group/a--package/a--class/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idGroupA) === "index.html#group/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idPackageA) === "group_a.html#group/a--package/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idClassA) === "group_a--package_a.html#group/a--package/a--class/a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageA, SampleData.idGroupA) === "index.html#group/a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageA, SampleData.idPackageA) === "group_a.html#group/a--package/a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageA, SampleData.idClassA) === "#group/a--package/a--class/a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageB, SampleData.idGroupA) === "index.html#group/a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageB, SampleData.idPackageA) === "group_a.html#group/a--package/a")
    assert(HtmlUtil.htmlLink(SampleData.idPackageB, SampleData.idClassA) === "group_a--package_a.html#group/a--package/a--class/a")
  }
  /*
    assert(reportTransformer.htmlLinkRelative(unitId) === "#g/a--p/b-p/c-p/d--c/e-c/f")
    assert(reportTransformer.htmlLinkAbsolute(unitId) === "g_a--p_b-p_c-p_d.html#g/a--p/b-p/c-p/d--c/e-c/f")
   */
}
