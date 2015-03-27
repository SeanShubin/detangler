package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class ReportTransformerTest extends FunSuite {
  ignore("root report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.rootReport(SampleData.detangled)
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
    assert(groupB.anchor.link === "index.html#group/b")
    assert(groupB.depth === "3")
    assert(groupB.complexity === "4")
    assert(groupB.reasonAnchor.name === "reason")
    assert(groupB.reasonAnchor.link === "index.html#group/a_group/b")
    assert(page.reasons.size === 1)
    val groupAtoGroupB = page.reasons.head
    assert(groupAtoGroupB.from.name === "group/a")
    assert(groupAtoGroupB.from.link === "index.html#group/a")
    assert(groupAtoGroupB.to.name === "group/b")
    assert(groupAtoGroupB.to.link === "index.html#group/b")
    assert(groupAtoGroupB.reasons.size === 1)
    val packageAtoPackageB = groupAtoGroupB.reasons.head
    assert(packageAtoPackageB.from.name === "package/a")
    assert(packageAtoPackageB.from.link === "index.html#group/a_package/a")
    assert(packageAtoPackageB.to.name === "package/b")
    assert(packageAtoPackageB.to.link === "index.html#group/b_package/c")
    assert(packageAtoPackageB.reasons.size === 1)
    val classAtoClassD = packageAtoPackageB.reasons.head
    assert(classAtoClassD.from.name === "class/a")
    assert(classAtoClassD.from.link === "index.html#group/a_package/a_class/a")
    assert(classAtoClassD.to.name === "class/b")
    assert(classAtoClassD.to.link === "index.html#group/b_package/c_class/d")
    assert(classAtoClassD.reasons.size === 0)
  }

  ignore("html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val classLevelUnitId = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"), Set("c/e", "c/f"))
    assert(reportTransformer.htmlId(classLevelUnitId) === "g/a--p/b-p/c-p/d--c/e-c/f")
    assert(reportTransformer.htmlName(classLevelUnitId) === "c/e-c/f")
    assert(reportTransformer.htmlAnchor(classLevelUnitId) === "g_a--p_b-p_c-p_d--c_e-c_f.html#g/a--p/b-p/c-p/d--c/e-c/f")
    val packageLevelUnitId = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"))
  }
}
