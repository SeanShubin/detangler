package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import com.seanshubin.detangler.core.html.{HtmlPage, HtmlUnit}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class PageGeneratorImpl(resourceLoader: ResourceLoader) extends PageGenerator {
  override def generatePageText(page: HtmlPage): String = {
    val pageTemplate = loadTemplate("page")
    val unitSummaryTemplate = loadTemplate("unit-summary")
    val unitSummaryTable = exactlyOneElement(unitSummaryTemplate, "body table")
    val unitSummaries = page.units.map(htmlUnit => generateUnitSummary(htmlUnit, unitSummaryTemplate))
    unitSummaries.foreach(unitSummaryTable.appendChild)
    pageTemplate.body().appendChild(unitSummaryTable)
    pageTemplate.toString
  }

  private def generateUnitSummary(htmlUnit: HtmlUnit, unitSummaryTemplate: Element): Element = {
    val unitSummaryClone = unitSummaryTemplate.clone()
    setText(unitSummaryClone, "table tbody tr td:eq(0)", htmlUnit.name)
    unitSummaryClone
  }

  private def loadTemplate(baseName: String): Document = {
    val name = s"$baseName.html"
    val in = resourceLoader.inputStreamFor(name)
    val charset = StandardCharsets.UTF_8
    val charsetName = charset.name()
    val doc: Document = Jsoup.parse(in, charsetName, "")
    doc
  }

  private def setText(element: Element, cssSelector: String, value: String): Unit = {
    exactlyOneElement(element, cssSelector).text(value)
  }

  private def exactlyOneElement(element: Element, cssQuery: String): Element = {
    val elements = element.select(cssQuery)
    val size = elements.size
    if (size == 1) elements.get(0)
    else throw new RuntimeException(s"Expected exactly one element from '$cssQuery', got $size")
  }
}
