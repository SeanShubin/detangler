package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import com.seanshubin.detangler.core.html.{HtmlPage, HtmlUnit}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class PageGeneratorImpl(detangled:Detangled, resourceLoader: ResourceLoader) extends PageGenerator {
  override def generatePageText(page: HtmlPage): String = {
    val pageTemplate = loadTemplate("page")
    val unitSummaryTemplate = loadTemplate("unit-summary")
    val unitDetailTemplate = loadTemplate("unit-detail")
    val unitSummaryTable = exactlyOneElement(unitSummaryTemplate, "body table")
    val unitDetailList = exactlyOneElement(unitDetailTemplate, "body ul")
    page.units.foreach(attachUnitSummary(pageTemplate.body(), _, unitSummaryTable, unitDetailList))
    pageTemplate.outputSettings().indentAmount(2)
    pageTemplate.toString
  }

  override def pageForId(id: UnitId): String = {
    val pageTemplate = loadTemplate("page")
    val unitIds = detangled.composedOf(id)
    val pageBody = pageTemplate.body()
    unitIds.foreach(appendUnitInfo(_, pageBody))
    pageTemplate.toString
  }

  private def appendUnitInfo(unitId:UnitId, element:Element): Unit = {
    appendUnitSummary(unitId, element)
    appendUnitDetail(unitId, element)
  }

  private def appendUnitSummary(unitId:UnitId, element:Element):Unit ={
    val unitSummaryTemplate = loadTemplate("unit-summary")
    val unitSummaryTable = exactlyOneElement(unitSummaryTemplate, "body table")
    unitSummaryTable.attr("id", HtmlUtil.htmlId(unitId))
    exactlyOneElement(unitSummaryTable, "tbody tr td:eq(0)").text(HtmlUtil.htmlName(unitId))
    exactlyOneElement(unitSummaryTable, "tbody tr td:eq(1)").text(detangled.depth(unitId).toString)
    exactlyOneElement(unitSummaryTable, "tbody tr td:eq(2)").text(detangled.complexity(unitId).toString)
    exactlyOneElement(unitSummaryTable, "tbody tr td:eq(3) a").text("parts")
    exactlyOneElement(unitSummaryTable, "tbody tr td:eq(3) a").attr("href", HtmlUtil.fileNameFor(unitId))
    element.appendChild(unitSummaryTable)
  }

  private def appendUnitDetail(unitId:UnitId, element:Element):Unit ={
    val unitDetailTemplate = loadTemplate("unit-detail")
    val unitDetailList = exactlyOneElement(unitDetailTemplate, "body ul")
    text(unitDetailList, "li table thead tr:eq(0) th", dependsOnCaption(unitId))
    element.appendChild(unitDetailList)
  }

  private def dependsOnCaption(unitId:UnitId):String = {
    val dependsOn = detangled.dependsOn(unitId)
    s"depends on (${dependsOn.size})"
  }

  private def attachUnitSummary(target: Element, htmlUnit: HtmlUnit, unitSummaryTemplate: Element, unitDetailTemplate: Element): Unit = {
    val unitSummaryClone = unitSummaryTemplate.clone()
    unitSummaryClone.attr("id", htmlUnit.id)
    exactlyOneElement(unitSummaryClone, "tbody tr td:eq(0)").text(htmlUnit.name)
    exactlyOneElement(unitSummaryClone, "tbody tr td:eq(1)").text(htmlUnit.depth)
    exactlyOneElement(unitSummaryClone, "tbody tr td:eq(2)").text(htmlUnit.complexity)
    exactlyOneElement(unitSummaryClone, "tbody tr td:eq(3) a").text(htmlUnit.partsAnchor.name)
    exactlyOneElement(unitSummaryClone, "tbody tr td:eq(3) a").attr("href", htmlUnit.partsAnchor.link)
    target.appendChild(unitSummaryClone)

    val unitDetailTemplateClone = unitDetailTemplate.clone()
    text(unitDetailTemplateClone, "li table thead tr:eq(0) th", htmlUnit.dependsOnCaption)
    target.appendChild(unitDetailTemplateClone)
  }

  private def text(target: Element, cssSelector: String, value: String): Unit = {
    exactlyOneElement(target, cssSelector).text(value)
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
    else throw new RuntimeException(s"Expected exactly one element from '$cssQuery', got $size\n$element")
  }
}
