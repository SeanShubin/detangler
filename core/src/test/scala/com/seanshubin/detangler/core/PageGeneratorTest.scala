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
    assert(exactlyOneElement(table, ".reason").attr("href") === "#group_a---group_b")
  }

  def header(caption: String)(f: => Unit): Unit = {
    val targetLength = 80
    val starCount = (targetLength - caption.length) / 2
    val stars = "-" * starCount
    println("/" + stars + " " + caption + " " + stars + "\\")
    f
    println("\\" + stars + " " + caption + " " + stars + "/")
  }

  test("top page arrows") {
    val document = documentFor(SampleData.idRoot)
    val reasons = document.select(".reason")
    for {
      reasonIndex <- 0 until reasons.size()
      reason = reasons.get(reasonIndex)
    } {
      header(s"reason $reasonIndex") {
        println(reason)
      }
      println()
    }

    //    assert(reasons.size() === 3)
    //
    //    assert(exactlyOneElement(reasons.get(0), "#group_a---group_b .from").text() === "group/
    //    assert(exactlyOneElement(reasons.get(0), "#group_a---group_b .from").attr("href") === "#group_a")
    //    assert(exactlyOneElement(reasons.get(0), "#group_a---group_b .to").text() === "group/b")
    //    assert(exactlyOneElement(reasons.get(0), "#group_a---group_b .to").attr("href") === "#group_b")
    //
    //    assert(exactlyOneElement(reasons.get(1), "#group_a--package_a---group_b--package_c .from").text() === "package/a")
    //    assert(exactlyOneElement(reasons.get(1), "#group_a--package_a---group_b--package_c .from").attr("href") === "group_a.html#group_a--package_a")
    //    assert(exactlyOneElement(reasons.get(1), "#group_a--package_a---group_b--package_c .to").text() === "package/c")
    //    assert(exactlyOneElement(reasons.get(1), "#group_a--package_a---group_b--package_c .to").attr("href") === "group_b.html#group_b--package_c")
    //
    //    assert(exactlyOneElement(reasons.get(2), "#group_a--package_a--class_a---group_b--package_c--class_d .from").text() === "class/a")
    //    assert(exactlyOneElement(reasons.get(2), "#group_a--package_a--class_a---group_b--package_c--class_d .from").attr("href") === "group_a--package_a.html#group_a--package_a--class_a")
    //    assert(exactlyOneElement(reasons.get(2), "#group_a--package_a--class_a---group_b--package_c--class_d .to").text() === "class/d")
    //    assert(exactlyOneElement(reasons.get(2), "#group_a--package_a--class_a---group_b--package_c--class_d .to").attr("href") === "group_b--package_c.html#group_b--package_c--class_d")
  }

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
