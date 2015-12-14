package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class HtmlUtilTest extends FunSuite {
  test("file names") {
    assert(HtmlUtil.fileNameFor(SampleData.theRoot) === "index.html")
    assert(HtmlUtil.fileNameFor(SampleData.groupA) === "group-a.html")
    assert(HtmlUtil.fileNameFor(SampleData.packageC) === "group-a--package-c.html")
  }

  test("html id") {
    assert(HtmlUtil.htmlId(SampleData.groupA) === "group-a")
    assert(HtmlUtil.htmlId(SampleData.packageC) === "package-c")
    assert(HtmlUtil.htmlId(SampleData.classF) === "class-f")
  }

  test("html name") {
    assert(HtmlUtil.htmlName(SampleData.groupA) === "group/a")
    assert(HtmlUtil.htmlName(SampleData.packageC) === "package/c")
    assert(HtmlUtil.htmlName(SampleData.classF) === "class/f")
  }

  test("html link") {
    assert(HtmlUtil.htmlLink(SampleData.theRoot, SampleData.groupA) === "#group-a")
    assert(HtmlUtil.htmlLink(SampleData.theRoot, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlUtil.htmlLink(SampleData.theRoot, SampleData.classF) === "group-a--package-c.html#class-f")
    assert(HtmlUtil.htmlLink(SampleData.groupA, SampleData.groupA) === "index.html#group-a")
    assert(HtmlUtil.htmlLink(SampleData.groupA, SampleData.packageC) === "#package-c")
    assert(HtmlUtil.htmlLink(SampleData.groupA, SampleData.classF) === "group-a--package-c.html#class-f")
    assert(HtmlUtil.htmlLink(SampleData.groupB, SampleData.groupA) === "index.html#group-a")
    assert(HtmlUtil.htmlLink(SampleData.groupB, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlUtil.htmlLink(SampleData.groupB, SampleData.classF) === "group-a--package-c.html#class-f")
    assert(HtmlUtil.htmlLink(SampleData.packageC, SampleData.groupA) === "index.html#group-a")
    assert(HtmlUtil.htmlLink(SampleData.packageC, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlUtil.htmlLink(SampleData.packageC, SampleData.classF) === "#class-f")
    assert(HtmlUtil.htmlLink(SampleData.packageD, SampleData.groupA) === "index.html#group-a")
    assert(HtmlUtil.htmlLink(SampleData.packageD, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlUtil.htmlLink(SampleData.packageD, SampleData.classF) === "group-a--package-c.html#class-f")
  }
}
