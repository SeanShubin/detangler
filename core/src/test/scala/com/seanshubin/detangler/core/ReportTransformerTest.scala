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
    assert(groupB.reasonAnchor.link === "index.html#group/a---group/b")
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

  ignore("middle report") {
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
    assert(packageB.anchor.link === "#group/b--package/b")
    assert(packageB.depth === "7")
    assert(packageB.complexity === "8")
    assert(packageB.reasonAnchor.name === "reason")
    assert(packageB.reasonAnchor.link === "#group/a--package/a---group/b--package/b")
    assert(page.reasons.size === 1)
    val packageAToPackageB = page.reasons.head
    assert(packageAToPackageB.from.name === "package/a")
    assert(packageAToPackageB.from.link === "#group/a--package/a")
    assert(packageAToPackageB.to.name === "package/b")
    assert(packageAToPackageB.to.link === "#group/b--package/b")
    assert(packageAToPackageB.reasons.size === 1)
    val classAtoClassD = packageAToPackageB.reasons.head
    assert(classAtoClassD.from.name === "class/a")
    assert(classAtoClassD.from.link === "group_a--package_a.html#group/a--package/a--class/a")
    assert(classAtoClassD.to.name === "class/d")
    assert(classAtoClassD.to.link === "group_b--package_c.html#group/b--package/c--class/d")
    assert(classAtoClassD.reasons.size === 0)
  }

  ignore("bottom report") {
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
    assert(classB.anchor.link === "#group/b--package/b--class/b")
    assert(classB.depth === "13")
    assert(classB.complexity === "14")
  }

  test("class level unit html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val unitId = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"), Set("c/e", "c/f"))
    assert(reportTransformer.htmlId(unitId) === "g/a--p/b-p/c-p/d--c/e-c/f")
    assert(reportTransformer.htmlName(unitId) === "c/e-c/f")
    assert(reportTransformer.htmlLinkRelative(unitId) === "#g/a--p/b-p/c-p/d--c/e-c/f")
    assert(reportTransformer.htmlLinkAbsolute(unitId) === "g_a--p_b-p_c-p_d.html#g/a--p/b-p/c-p/d--c/e-c/f")
    assert(reportTransformer.htmlFileName(unitId) === "g_a--p_b-p_c-p_d.html")
  }

  test("package level unit html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val unitId = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"))
    assert(reportTransformer.htmlId(unitId) === "g/a--p/b-p/c-p/d")
    assert(reportTransformer.htmlName(unitId) === "p/b-p/c-p/d")
    assert(reportTransformer.htmlLinkRelative(unitId) === "#g/a--p/b-p/c-p/d")
    assert(reportTransformer.htmlLinkAbsolute(unitId) === "g_a.html#g/a--p/b-p/c-p/d")
    assert(reportTransformer.htmlFileName(unitId) === "g_a.html")
  }

  test("root level unit html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val unitId = SampleData.idRoot
    assert(reportTransformer.htmlFileName(unitId) === "index.html")
  }

  ignore("top level unit html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val unitId = SampleData.idGroupA
    assert(reportTransformer.htmlId(unitId) === "group/a")
    assert(reportTransformer.htmlName(unitId) === "group/a")
    assert(reportTransformer.htmlLinkRelative(unitId) === "#group/a")
    assert(reportTransformer.htmlLinkAbsolute(unitId) === "index.html#group/a")
    assert(reportTransformer.htmlFileName(unitId) === "index.html")
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
