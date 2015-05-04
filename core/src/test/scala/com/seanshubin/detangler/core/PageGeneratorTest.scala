package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.scalatest.FunSuite
import JsoupUtil.exactlyOneElement

class PageGeneratorTest extends FunSuite {
  test("top page summary") {
    val document = documentFor(SampleData.idRoot)
    document.outputSettings().indentAmount(2)
    assert(document.select(".unit-div").size() === 2)
    val table = exactlyOneElement(document, "#group_a .unit-summary")
    assert(exactlyOneElement(table, ".name").text() === "group/a")
    assert(exactlyOneElement(table, ".depth").text() === "1")
    assert(exactlyOneElement(table, ".complexity").text() === "2")
    assert(exactlyOneElement(table, ".composed-of").text() === "parts")
    assert(exactlyOneElement(table, ".composed-of").attr("href") === "group_a.html")
  }

  test("top page depends on") {
    val document = documentFor(SampleData.idRoot)
    val table = exactlyOneElement(document, "#group_a .unit-depends-on")
    assert(exactlyOneElement(table, ".depends-on-caption").text() === "depends on (1)")
    assert(exactlyOneElement(table, ".name").text() === "group/b")
    assert(exactlyOneElement(table, ".name").attr("href") === "#group_b")
    assert(exactlyOneElement(table, ".depth").text() === "3")
    assert(exactlyOneElement(table, ".complexity").text() === "4")
    assert(exactlyOneElement(table, ".reason").text() === "reason")
    assert(exactlyOneElement(table, ".reason").attr("href") === "#group_a---group_b")
  }

  def assertText(element:Element, expected:String): Unit = {
    assert(element.text() === expected)
  }

  def assertAnchor(element:Element, expectedText:String, expectedLink:String): Unit = {
    assert(element.text() === expectedText)
    assert(element.attr("href") === expectedLink)
  }

  def documentFor(unitId:UnitId):Document = {
    val classLoader:ClassLoader = this.getClass.getClassLoader
    val classLoaderIntegration:ClassLoaderIntegration = new ClassLoaderIntegrationImpl(classLoader)
    val resourceLoader:ResourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
    val pageGenerator:PageGenerator = new PageGeneratorImpl(SampleData.detangled, resourceLoader)
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
