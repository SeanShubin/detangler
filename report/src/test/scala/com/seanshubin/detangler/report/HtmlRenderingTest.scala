package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class HtmlRenderingTest extends FunSuite {
  test("file names") {
    assert(HtmlRendering.fileNameFor(SampleData.root) === "index.html")
    assert(HtmlRendering.fileNameFor(SampleData.groupA) === "group-a.html")
    assert(HtmlRendering.fileNameFor(SampleData.packageC) === "group-a--package-c.html")
  }

  test("html id") {
    assert(HtmlRendering.htmlId(SampleData.groupA) === "group-a")
    assert(HtmlRendering.htmlId(SampleData.packageC) === "package-c")
    assert(HtmlRendering.htmlId(SampleData.classF) === "class-f")
  }

  test("html name") {
    assert(HtmlRendering.htmlName(SampleData.groupA) === "group/a")
    assert(HtmlRendering.htmlName(SampleData.packageC) === "package/c")
    assert(HtmlRendering.htmlName(SampleData.classF) === "class/f")
  }

  test("html link") {
    assert(HtmlRendering.htmlLink(SampleData.root, SampleData.groupA) === "#group-a")
    assert(HtmlRendering.htmlLink(SampleData.root, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlRendering.htmlLink(SampleData.root, SampleData.classF) === "group-a--package-c.html#class-f")
    assert(HtmlRendering.htmlLink(SampleData.groupA, SampleData.groupA) === "index.html#group-a")
    assert(HtmlRendering.htmlLink(SampleData.groupA, SampleData.packageC) === "#package-c")
    assert(HtmlRendering.htmlLink(SampleData.groupA, SampleData.classF) === "group-a--package-c.html#class-f")
    assert(HtmlRendering.htmlLink(SampleData.groupB, SampleData.groupA) === "index.html#group-a")
    assert(HtmlRendering.htmlLink(SampleData.groupB, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlRendering.htmlLink(SampleData.groupB, SampleData.classF) === "group-a--package-c.html#class-f")
    assert(HtmlRendering.htmlLink(SampleData.packageC, SampleData.groupA) === "index.html#group-a")
    assert(HtmlRendering.htmlLink(SampleData.packageC, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlRendering.htmlLink(SampleData.packageC, SampleData.classF) === "#class-f")
    assert(HtmlRendering.htmlLink(SampleData.packageD, SampleData.groupA) === "index.html#group-a")
    assert(HtmlRendering.htmlLink(SampleData.packageD, SampleData.packageC) === "group-a.html#package-c")
    assert(HtmlRendering.htmlLink(SampleData.packageD, SampleData.classF) === "group-a--package-c.html#class-f")
  }

  test("reason name") {
    assert(HtmlRendering.reasonName(SampleData.groupA, SampleData.groupB) === "reason")
    assert(HtmlRendering.reasonName(SampleData.packageC, SampleData.packageD) === "reason")
    assert(HtmlRendering.reasonName(SampleData.classF, SampleData.classG) === "reason")
  }

  test("reason link") {
    assert(HtmlRendering.reasonLink(SampleData.groupA, SampleData.groupB) === "#group-a---group-b")
    assert(HtmlRendering.reasonLink(SampleData.packageC, SampleData.packageD) === "#group-a--package-c---group-a--package-d")
    assert(HtmlRendering.reasonLink(SampleData.classF, SampleData.classG) === "#group-a--package-c--class-f---group-a--package-c--class-g")
  }
}
