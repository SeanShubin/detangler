package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import org.scalatest.FunSuite

import scala.collection.JavaConversions

class JsoupTest extends FunSuite {
  test("compose page with table") {
    val tableDocument = loadResource("table.html")
    val pageDocument = loadResource("empty-page.html")
    val table = exactlyOneElement(tableDocument, "body table")
    val tableBody = exactlyOneElement(table, "tbody")
    val row = firstElement(tableBody, "tr").clone()
    replaceRowText(row, "aaa", "bbb", "ccc")
    val rows = elementSeq(tableBody, "tr")
    pageDocument.body().appendChild(table)
    rows.foreach(_.remove())
    tableBody.appendChild(row)

    assert(pageDocument.select("body table tbody tr").size() === 1, "only one row")
    assert(pageDocument.select("body table tbody tr td").size() === 3, "three cells in the row")
    assert(pageDocument.select("body table tbody tr td:eq(0)").text() === "aaa", "text for first cell")
    assert(pageDocument.select("body table tbody tr td:eq(1)").text() === "bbb", "text for second cell")
    assert(pageDocument.select("body table tbody tr td:eq(2)").text() === "ccc", "text for third cell")
  }

  def loadResource(name: String): Document = {
    val in = getClass.getClassLoader.getResourceAsStream(name)
    val charset = StandardCharsets.UTF_8
    val charsetName = charset.name()
    val doc: Document = Jsoup.parse(in, charsetName, "")
    doc
  }

  def exactlyOneElement(element: Element, cssQuery: String): Element = {
    val elements = element.select(cssQuery)
    val size = elements.size
    if (size == 1) elements.get(0)
    else throw new RuntimeException(s"Expected exactly one element from '$cssQuery', got $size")
  }

  def firstElement(element: Element, cssQuery: String): Element = {
    element.select(cssQuery).get(0)
  }

  def elementSeq(element: Element, cssQuery: String): Seq[Element] = {
    val elements: Elements = element.select(cssQuery)
    val elementsJavaIterator = elements.iterator()
    val elementsScalaIterator: Iterator[Element] = JavaConversions.asScalaIterator(elementsJavaIterator)
    val elementsSeq: Seq[Element] = elementsScalaIterator.toSeq
    elementsSeq
  }

  def replaceRowText(row: Element, textSeq: String*): Unit = {
    val dataSeq = elementSeq(row, "td")
    for {
      (data, text) <- dataSeq zip textSeq
    } {
      data.text(text)
    }
  }
}
