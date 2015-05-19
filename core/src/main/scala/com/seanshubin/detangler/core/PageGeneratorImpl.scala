package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import com.seanshubin.detangler.core.JsoupUtil.exactlyOneElement
import com.seanshubin.detangler.core.html.{HtmlPage, HtmlUnit}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class PageGeneratorImpl(detangled: Detangled, resourceLoader: ResourceLoader, removeClasses:Boolean) extends PageGenerator {
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
    val unitTemplate = loadTemplate("unit")
    val unitDependsOnRow = JsoupUtil.extractFragment(unitTemplate, "unit-depends-on-row", removeClasses)
    val unitDependsOn = JsoupUtil.extractFragment(unitTemplate, "unit-depends-on", removeClasses)
    val unitSummary = JsoupUtil.extractFragment(unitTemplate, "unit-summary", removeClasses)
    val unitDiv = JsoupUtil.extractFragment(unitTemplate, "unit-root", removeClasses)
    val unitIds = detangled.composedOf(id)
    val attachUnit = exactlyOneElement(unitTemplate.body(), ".attach-unit")
    unitIds.foreach(appendUnitInfo(_, id, attachUnit, unitDiv, unitSummary, unitDependsOn, unitDependsOnRow))
    unitTemplate.outputSettings().indentAmount(2)
    unitTemplate.toString
  }

  private def appendUnitInfo(unitId: UnitId,
                             pageUnitId:UnitId,
                             appendTo: Element,
                             unitDivOriginal: Element,
                             unitSummaryOriginal: Element,
                             unitDependsOnOriginal: Element,
                             unitDependsOnRowOriginal: Element): Unit = {
    val unitDiv = unitDivOriginal.clone()
    val unitSummary = unitSummaryOriginal.clone()
    val unitDependsOn = unitDependsOnOriginal.clone()
    val unitDependsOnRow = unitDependsOnRowOriginal.clone()
    unitDiv.attr("id", HtmlUtil.htmlId(unitId))
    appendUnitSummary(unitId, unitDiv, unitSummary)
    appendDependsOn(unitId, pageUnitId, unitDiv, unitDependsOn, unitDependsOnRow)
    appendTo.appendChild(unitDiv)
  }

  private def appendUnitSummary(unitId: UnitId, appendTo: Element, unitSummary: Element): Unit = {
    JsoupUtil.setText(unitSummary, "name", HtmlUtil.htmlName(unitId), removeClasses)
    JsoupUtil.setText(unitSummary, "depth", detangled.depth(unitId).toString, removeClasses)
    JsoupUtil.setText(unitSummary, "complexity", detangled.complexity(unitId).toString, removeClasses)
    JsoupUtil.setAnchor(unitSummary, "composed-of", "parts", HtmlUtil.fileNameFor(unitId), removeClasses)
    appendTo.appendChild(unitSummary)
  }

  private def appendDependsOn(unitId: UnitId, pageUnitId:UnitId, element: Element, unitDetailListOriginal: Element, unitDependsOnRow: Element): Unit = {
    val unitDetailList = unitDetailListOriginal.clone()
    val dependsOnUnits = detangled.dependsOn(unitId)
    val size = dependsOnUnits.size
    JsoupUtil.setText(unitDetailList, "depends-on-caption", s"depends on ($size)", removeClasses)
    val attachRowsTo = exactlyOneElement(unitDetailList, ".attach-unit-depends-on-row")
    dependsOnUnits.foreach(appendUnitDetailRow(_, unitId, pageUnitId, attachRowsTo, unitDependsOnRow))
    element.appendChild(unitDetailList)
  }

  private def appendUnitDetailRow(unitId: UnitId, from: UnitId, pageUnitId:UnitId, element: Element, unitDependsOnRowOriginal: Element): Unit = {
    val unitDependsOnRow = unitDependsOnRowOriginal.clone()
    JsoupUtil.setAnchor(unitDependsOnRow, "name", HtmlUtil.htmlName(unitId), HtmlUtil.htmlLink(pageUnitId, unitId), removeClasses)
    JsoupUtil.setText(unitDependsOnRow, "depth", detangled.depth(unitId).toString, removeClasses)
    JsoupUtil.setText(unitDependsOnRow, "complexity", detangled.complexity(unitId).toString, removeClasses)
    JsoupUtil.setAnchor(unitDependsOnRow, "reason", HtmlUtil.arrowName(from, unitId), HtmlUtil.arrowLink(from, unitId), removeClasses)
    element.appendChild(unitDependsOnRow)
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
}

/*
Types of things to do with templates

What do we append to at the top level?
load the top level page, append to its body, convert the top level page to a string

What do we append at the top level?
load the relevant section from a template by id, then remove the id

What do we append to?
clone the template



*/