package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class ReportTransformerTest extends FunSuite {
  test("top report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.pageFor(SampleData.detangled, SampleData.idRoot)
    assert(page.fileName === "index.html")
    assert(page.units.size === 2)
    val groupA = page.units.head
    assert(groupA.id === "group/a")
    assert(groupA.name === "group/a")
    assert(groupA.depth === "1")
    assert(groupA.complexity === "2")
    assert(groupA.partsAnchor.name === "parts")
    assert(groupA.partsAnchor.link === "group_a.html")
    assert(groupA.dependsOn.size === 1)
    assert(groupA.dependedOnBy.size === 0)
    assert(groupA.dependsOnExternal.size === 0)
    assert(groupA.dependedOnByExternal.size === 0)
    val groupB = groupA.dependsOn.head
    assert(groupB.anchor.name === "group/b")
    assert(groupB.anchor.link === "#group/b")
    assert(groupB.depth === "3")
    assert(groupB.complexity === "4")
    assert(groupB.reasonAnchor.name === "reason")
    assert(groupB.reasonAnchor.link === "#group/a---group/b")
    assert(page.reasons.size === 1)
    val groupAtoGroupB = page.reasons.head
    assert(groupAtoGroupB.from.name === "group/a")
    assert(groupAtoGroupB.from.link === "#group/a")
    assert(groupAtoGroupB.to.name === "group/b")
    assert(groupAtoGroupB.to.link === "#group/b")
    assert(groupAtoGroupB.reasons.size === 1)
    val packageAtoPackageB = groupAtoGroupB.reasons.head
    assert(packageAtoPackageB.from.name === "package/a")
    assert(packageAtoPackageB.from.link === "group_a.html#group/a--package/a")
    assert(packageAtoPackageB.to.name === "package/c")
    assert(packageAtoPackageB.to.link === "group_b.html#group/b--package/c")
    assert(packageAtoPackageB.reasons.size === 1)
    val classAtoClassD = packageAtoPackageB.reasons.head
    assert(classAtoClassD.from.name === "class/a")
    assert(classAtoClassD.from.link === "group_a--package_a.html#group/a--package/a--class/a")
    assert(classAtoClassD.to.name === "class/d")
    assert(classAtoClassD.to.link === "group_b--package_c.html#group/b--package/c--class/d")
    assert(classAtoClassD.reasons.size === 0)
  }

  test("middle report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.pageFor(SampleData.detangled, SampleData.idGroupA)
    assert(page.fileName === "group_a.html")
    assert(page.units.size === 2)
    val packageA = page.units.head
    assert(packageA.id === "group/a--package/a")
    assert(packageA.name === "package/a")
    assert(packageA.depth === "5")
    assert(packageA.complexity === "6")
    assert(packageA.partsAnchor.name === "parts")
    assert(packageA.partsAnchor.link === "group_a--package_a.html")
    assert(packageA.dependsOn.size === 2)
    assert(packageA.dependedOnBy.size === 0)
    assert(packageA.dependsOnExternal.size === 0)
    assert(packageA.dependedOnByExternal.size === 0)
    val packageB = packageA.dependsOn.head
    assert(packageB.anchor.name === "package/b")
    assert(packageB.anchor.link === "#group/a--package/b")
    assert(packageB.depth === "7")
    assert(packageB.complexity === "8")
    assert(packageB.reasonAnchor.name === "reason")
    assert(packageB.reasonAnchor.link === "#group/a--package/a---group/a--package/b")
    assert(page.reasons.size === 1)
    val packageAToPackageB = page.reasons.head
    assert(packageAToPackageB.from.name === "package/a")
    assert(packageAToPackageB.from.link === "#group/a--package/a")
    assert(packageAToPackageB.to.name === "package/b")
    assert(packageAToPackageB.to.link === "#group/a--package/b")
    assert(packageAToPackageB.reasons.size === 1)
    val classAtoClassC = packageAToPackageB.reasons.head
    assert(classAtoClassC.from.name === "class/a")
    assert(classAtoClassC.from.link === "group_a--package_a.html#group/a--package/a--class/a")
    assert(classAtoClassC.to.name === "class/c")
    assert(classAtoClassC.to.link === "group_a--package_b.html#group/a--package/b--class/c")
    assert(classAtoClassC.reasons.size === 0)
  }

  test("bottom report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.pageFor(SampleData.detangled, SampleData.idPackageA)
    assert(page.fileName === "group_a--package_a.html")
    assert(page.units.size === 2)
    val classA = page.units.head
    assert(classA.id === "group/a--package/a--class/a")
    assert(classA.name === "class/a")
    assert(classA.depth === "11")
    assert(classA.complexity === "12")
    assert(classA.dependsOn.size === 3)
    assert(classA.dependedOnBy.size === 0)
    assert(classA.dependsOnExternal.size === 0)
    assert(classA.dependedOnByExternal.size === 0)
    val classB = classA.dependsOn.head
    assert(classB.anchor.name === "class/b")
    assert(classB.anchor.link === "#group/a--package/a--class/b")
    assert(classB.depth === "13")
    assert(classB.complexity === "14")
  }

  test("class level unit html strings") {
    val unitId = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"), Set("c/e", "c/f"))
    val parent = unitId.parent
    assert(HtmlUtil.htmlId(unitId) === "g/a--p/b-p/c-p/d--c/e-c/f")
    assert(HtmlUtil.htmlName(unitId) === "c/e-c/f")
    assert(HtmlUtil.htmlLink(parent, unitId) === "#g/a--p/b-p/c-p/d--c/e-c/f")
    assert(HtmlUtil.htmlLink(UnitId.Root, unitId) === "g_a--p_b-p_c-p_d.html#g/a--p/b-p/c-p/d--c/e-c/f")
  }

  test("package level unit html strings") {
    val unitId = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"))
    val parent = unitId.parent
    assert(HtmlUtil.htmlId(unitId) === "g/a--p/b-p/c-p/d")
    assert(HtmlUtil.htmlName(unitId) === "p/b-p/c-p/d")
    assert(HtmlUtil.htmlLink(parent, unitId) === "#g/a--p/b-p/c-p/d")
    assert(HtmlUtil.htmlLink(UnitId.Root, unitId) === "g_a.html#g/a--p/b-p/c-p/d")
    assert(HtmlUtil.fileNameFor(unitId) === "g_a--p_b-p_c-p_d.html")
  }

  test("top level unit html strings") {
    val unitId = SampleData.idGroupA
    assert(HtmlUtil.htmlId(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlName(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlLink(UnitId.Root, SampleData.idGroupA) === "#group/a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idGroupA) === "index.html#group/a")
    assert(HtmlUtil.fileNameFor(unitId) === "group_a.html")
    assert(HtmlUtil.fileNameFor(UnitId.Root) === "index.html")
  }

  test("class level arrow html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val from = UnitId.simple("g/a", "p/a", "c/a")
    val to = UnitId.simple("g/b", "p/c", "c/d")
    assert(reportTransformer.arrowId(from, to) === "g/a--p/a--c/a---g/b--p/c--c/d")
    assert(reportTransformer.arrowName(from, to) === "reason")
  }

  test("package level arrow html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val from = UnitId.simple("g/a", "p/a")
    val to = UnitId.simple("g/b", "p/c")
    assert(reportTransformer.arrowId(from, to) === "g/a--p/a---g/b--p/c")
    assert(reportTransformer.arrowName(from, to) === "reason")
  }

  test("top level arrow html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val from = UnitId.simple("g/a")
    val to = UnitId.simple("g/b")
    assert(reportTransformer.arrowId(from, to) === "g/a---g/b")
    assert(reportTransformer.arrowName(from, to) === "reason")
  }
}
