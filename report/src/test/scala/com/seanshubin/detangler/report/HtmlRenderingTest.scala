package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class HtmlRenderingTest extends FunSuite {
  test("file names") {
    assert(HtmlRender.reportPageLink(SampleData.root) === "report.html")
    assert(HtmlRender.reportPageLink(SampleData.groupA) === "report--group-a.html")
    assert(HtmlRender.reportPageLink(SampleData.packageC) === "report--group-a--package-c.html")
  }

  test("html id") {
    assert(HtmlRender.id(SampleData.groupA) === "group-a")
    assert(HtmlRender.id(SampleData.packageC) === "package-c")
    assert(HtmlRender.id(SampleData.classF) === "class-f")
  }

  test("html name") {
    assert(HtmlRender.moduleLinkName(SampleData.groupA) === "group/a")
    assert(HtmlRender.moduleLinkName(SampleData.packageC) === "package/c")
    assert(HtmlRender.moduleLinkName(SampleData.classF) === "class/f")
  }

  test("html link") {
    assert(HtmlRender.moduleLink(SampleData.root, SampleData.groupA) === "#group-a")
    assert(HtmlRender.moduleLink(SampleData.root, SampleData.packageC) === "report--group-a.html#package-c")
    assert(HtmlRender.moduleLink(SampleData.root, SampleData.classF) === "report--group-a--package-c.html#class-f")
    assert(HtmlRender.moduleLink(SampleData.groupA, SampleData.groupA) === "report.html#group-a")
    assert(HtmlRender.moduleLink(SampleData.groupA, SampleData.packageC) === "#package-c")
    assert(HtmlRender.moduleLink(SampleData.groupA, SampleData.classF) === "report--group-a--package-c.html#class-f")
    assert(HtmlRender.moduleLink(SampleData.groupB, SampleData.groupA) === "report.html#group-a")
    assert(HtmlRender.moduleLink(SampleData.groupB, SampleData.packageC) === "report--group-a.html#package-c")
    assert(HtmlRender.moduleLink(SampleData.groupB, SampleData.classF) === "report--group-a--package-c.html#class-f")
    assert(HtmlRender.moduleLink(SampleData.packageC, SampleData.groupA) === "report.html#group-a")
    assert(HtmlRender.moduleLink(SampleData.packageC, SampleData.packageC) === "report--group-a.html#package-c")
    assert(HtmlRender.moduleLink(SampleData.packageC, SampleData.classF) === "#class-f")
    assert(HtmlRender.moduleLink(SampleData.packageD, SampleData.groupA) === "report.html#group-a")
    assert(HtmlRender.moduleLink(SampleData.packageD, SampleData.packageC) === "report--group-a.html#package-c")
    assert(HtmlRender.moduleLink(SampleData.packageD, SampleData.classF) === "report--group-a--package-c.html#class-f")
  }

  test("cycle id") {
    assert(HtmlRender.id(SampleDataWithCycles.cycleAB) === "cycle-group-a")
    assert(HtmlRender.id(SampleDataWithCycles.cycleCD) === "cycle-package-c")
    assert(HtmlRender.id(SampleDataWithCycles.cycleFG) === "cycle-class-f")
  }

  test("cycle link") {
    assert(HtmlRender.relativeModuleLink(SampleDataWithCycles.cycleAB) === "#cycle-group-a")
    assert(HtmlRender.relativeModuleLink(SampleDataWithCycles.cycleCD) === "#cycle-package-c")
    assert(HtmlRender.relativeModuleLink(SampleDataWithCycles.cycleFG) === "#cycle-class-f")
  }

  test("reason name") {
    assert(HtmlRender.reasonLinkName(SampleData.groupA, SampleData.groupB) === "reason")
    assert(HtmlRender.reasonLinkName(SampleData.packageC, SampleData.packageD) === "reason")
    assert(HtmlRender.reasonLinkName(SampleData.classF, SampleData.classG) === "reason")
  }

  test("reason link") {
    assert(HtmlRender.reasonLink(SampleData.groupA, SampleData.groupB) === "#group-a---group-b")
    assert(HtmlRender.reasonLink(SampleData.packageC, SampleData.packageD) === "#group-a--package-c---group-a--package-d")
    assert(HtmlRender.reasonLink(SampleData.classF, SampleData.classG) === "#group-a--package-c--class-f---group-a--package-c--class-g")
  }
}
