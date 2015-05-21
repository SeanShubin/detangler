package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.JsoupUtil.exactlyOneElement
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.scalatest.FunSuite

class PageGeneratorTest extends FunSuite {
  val shouldRemoveClass = false
  test("top page summary") {
    val document = documentFor(SampleData.idRoot)
    document.outputSettings().indentAmount(2)
    assert(document.select(".unit-root").size() === 2)
    val table = exactlyOneElement(document, "#group_a .unit-summary")
    assert(exactlyOneElement(table, ".name").text() === "group/a")
    assert(exactlyOneElement(table, ".depth").text() === "1")
    assert(exactlyOneElement(table, ".complexity").text() === "2")
    assert(exactlyOneElement(table, ".composed-of").text() === "parts")
    assert(exactlyOneElement(table, ".composed-of").attr("href") === "group_a.html")
  }

  test("top page depends on") {
    val document = documentFor(SampleData.idRoot)
    val table = exactlyOneElement(document, "#group_a .unit-dependency")
    assert(exactlyOneElement(table, ".caption").text() === "depends on (1)")
    assert(exactlyOneElement(table, ".name").text() === "group/b")
    assert(exactlyOneElement(table, ".name").attr("href") === "#group_b")
    assert(exactlyOneElement(table, ".depth").text() === "3")
    assert(exactlyOneElement(table, ".complexity").text() === "4")
    assert(exactlyOneElement(table, ".reason").text() === "reason")
    assert(exactlyOneElement(table, ".reason").attr("href") === "#group_a---group_b")
  }

  test("top page depended on by") {
    val document = documentFor(SampleData.idRoot)
    val table = exactlyOneElement(document, "#group_b .unit-dependency")
    assert(exactlyOneElement(table, ".caption").text() === "depended on by (1)")
    assert(exactlyOneElement(table, ".name").text() === "group/a")
    assert(exactlyOneElement(table, ".name").attr("href") === "#group_a")
    assert(exactlyOneElement(table, ".depth").text() === "1")
    assert(exactlyOneElement(table, ".complexity").text() === "2")
    assert(exactlyOneElement(table, ".reason").text() === "reason")
    //    assert(exactlyOneElement(table, ".reason").attr("href") === "#group_a---group_b")
  }

  /*
    val groupB = groupA.dependsOn.head
    assert(groupB.anchor.name === "group/b")
    assert(groupB.anchor.link === "#group_b")
    assert(groupB.depth === "3")
    assert(groupB.complexity === "4")
    assert(groupB.reasonAnchor.name === "reason")
    assert(groupB.reasonAnchor.link === "#group_a---group_b")
    assert(page.reasons.size === 1)
    val groupAtoGroupB = page.reasons.head
    assert(groupAtoGroupB.from.name === "group/a")
    assert(groupAtoGroupB.from.link === "#group_a")
    assert(groupAtoGroupB.to.name === "group/b")
    assert(groupAtoGroupB.to.link === "#group_b")
    assert(groupAtoGroupB.reasons.size === 1)
    val packageAtoPackageB = groupAtoGroupB.reasons.head
    assert(packageAtoPackageB.from.name === "package/a")
    assert(packageAtoPackageB.from.link === "group_a.html#group_a--package_a")
    assert(packageAtoPackageB.to.name === "package/c")
    assert(packageAtoPackageB.to.link === "group_b.html#group_b--package_c")
    assert(packageAtoPackageB.reasons.size === 1)
    val classAtoClassD = packageAtoPackageB.reasons.head
    assert(classAtoClassD.from.name === "class/a")
    assert(classAtoClassD.from.link === "group_a--package_a.html#group_a--package_a--class_a")
    assert(classAtoClassD.to.name === "class/d")
    assert(classAtoClassD.to.link === "group_b--package_c.html#group_b--package_c--class_d")
    assert(classAtoClassD.reasons.size === 0)
   */

  def assertText(element: Element, expected: String): Unit = {
    assert(element.text() === expected)
  }

  def assertAnchor(element: Element, expectedText: String, expectedLink: String): Unit = {
    assert(element.text() === expectedText)
    assert(element.attr("href") === expectedLink)
  }

  def documentFor(unitId: UnitId): Document = {
    val classLoader: ClassLoader = this.getClass.getClassLoader
    val classLoaderIntegration: ClassLoaderIntegration = new ClassLoaderIntegrationImpl(classLoader)
    val resourceLoader: ResourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
    val pageGenerator: PageGenerator = new PageGeneratorImpl(SampleData.detangled, resourceLoader, shouldRemoveClass)
    val page = pageGenerator.pageForId(SampleData.idRoot)
    val document = Jsoup.parse(page)
    document
  }
}

/*
Sample javascript selectors, useful for debugging from the browser

document.querySelectorAll('#group_a thead tr th').item(0).textContent
document.querySelectorAll('#group_a tbody tr td').item(0).textContent
document.querySelectorAll('#group_a tbody tr td').item(3).querySelector('a').text
document.querySelectorAll('#group_a tbody tr td').item(3).querySelector('a').attributes.href.nodeValue
*/
