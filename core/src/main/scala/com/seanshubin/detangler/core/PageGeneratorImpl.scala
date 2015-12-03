package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import com.seanshubin.detangler.core.JsoupUtil.exactlyOneElement
import com.seanshubin.detangler.core.html.{HtmlPage, HtmlUnit}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

class PageGeneratorImpl(detangled: Detangled, resourceLoader: ResourceLoader, removeClasses: Boolean) extends PageGenerator {
  private val jsoupUtil = new JsoupUtil(removeClasses)

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
    val templates = Templates.fromDocument(unitTemplate, removeClasses)
    val unitIds = detangled.composedOf(id)
    val attachUnit = exactlyOneElement(unitTemplate.body(), ".attach-unit")
    unitIds.foreach(appendUnitInfo(_, id, attachUnit, templates))
    appendReasons(unitTemplate, templates.reasons, templates.reason, id)
    unitTemplate.outputSettings().indentAmount(2)
    unitTemplate.toString
  }

  private def appendUnitInfo(unitId: UnitId,
                             pageUnitId: UnitId,
                             appendTo: Element,
                             templates: Templates): Unit = {
    val unitDiv = templates.unitDiv.clone()
    val unitSummary = templates.unitSummary.clone()
    val unitDependsOn = templates.unitDependency.clone()
    val unitDependsOnRow = templates.unitDependencyRow.clone()
    val unitDependedOnBy = templates.unitDependency.clone()
    val unitDependedOnByRow = templates.unitDependencyRow.clone()
    unitDiv.attr("id", HtmlUtil.htmlId(unitId))
    appendUnitSummary(unitId, unitDiv, unitSummary)
    appendDependsOn(unitId, pageUnitId, unitDiv, unitDependsOn, unitDependsOnRow)
    appendDependedOnBy(unitId, pageUnitId, unitDiv, unitDependedOnBy, unitDependedOnByRow)
    appendTo.appendChild(unitDiv)
  }

  private def appendUnitSummary(unitId: UnitId, appendTo: Element, unitSummary: Element): Unit = {
    jsoupUtil.setText(unitSummary, "name", HtmlUtil.htmlName(unitId))
    jsoupUtil.setText(unitSummary, "depth", detangled.depth(unitId).toString)
    jsoupUtil.setText(unitSummary, "complexity", detangled.complexity(unitId).toString)
    jsoupUtil.setAnchor(unitSummary, "composed-of", "parts", HtmlUtil.fileNameFor(unitId))
    appendTo.appendChild(unitSummary)
  }

  private def appendDependsOn(unitId: UnitId, pageUnitId: UnitId, element: Element, unitDetailListOriginal: Element, unitDependsOnRow: Element): Unit = {
    val caption = "depends on"
    val dependencyUnits = detangled.dependsOn(unitId)
    appendDependencies(unitId, pageUnitId, element, unitDetailListOriginal, unitDependsOnRow, caption, dependencyUnits, arrowDirection = true)
  }

  private def appendDependedOnBy(unitId: UnitId, pageUnitId: UnitId, element: Element, unitDetailListOriginal: Element, unitDependsOnRow: Element): Unit = {
    val caption = "depended on by"
    val dependencyUnits = detangled.dependedOnBy(unitId)
    appendDependencies(unitId, pageUnitId, element, unitDetailListOriginal, unitDependsOnRow, caption, dependencyUnits, arrowDirection = false)
  }

  private def appendDependencies(unitId: UnitId,
                                 pageUnitId: UnitId,
                                 element: Element,
                                 unitDetailListOriginal: Element,
                                 unitDependsOnRow: Element,
                                 caption: String,
                                 dependencyUnits: Seq[UnitId],
                                 arrowDirection: Boolean): Unit = {
    val size = dependencyUnits.size
    if (size > 0) {
      val unitDetailList = unitDetailListOriginal.clone()
      jsoupUtil.setText(unitDetailList, "caption", s"$caption ($size)")
      val attachRowsTo = exactlyOneElement(unitDetailList, ".attach-unit-dependency-row")
      dependencyUnits.foreach(appendUnitDetailRow(_, unitId, pageUnitId, attachRowsTo, unitDependsOnRow, arrowDirection))
      element.appendChild(unitDetailList)
    }
  }

  private def appendUnitDetailRow(unitId: UnitId, from: UnitId, pageUnitId: UnitId, element: Element, unitDependsOnRowOriginal: Element, arrowDirection: Boolean): Unit = {
    val (arrowName, arrowLink) = if (arrowDirection) {
      (HtmlUtil.arrowName(from, unitId), HtmlUtil.arrowLink(from, unitId))
    } else {
      (HtmlUtil.arrowName(unitId, from), HtmlUtil.arrowLink(unitId, from))
    }
    val unitDependsOnRow = unitDependsOnRowOriginal.clone()
    jsoupUtil.setAnchor(unitDependsOnRow, "name", HtmlUtil.htmlName(unitId), HtmlUtil.htmlLink(pageUnitId, unitId))
    jsoupUtil.setText(unitDependsOnRow, "depth", detangled.depth(unitId).toString)
    jsoupUtil.setText(unitDependsOnRow, "complexity", detangled.complexity(unitId).toString)
    jsoupUtil.setAnchor(unitDependsOnRow, "reason", arrowName, arrowLink)
    element.appendChild(unitDependsOnRow)
  }

  private def appendReasons(template: Document, listTemplate: Element, elementTemplate: Element, id: UnitId): Unit = {
    val arrows = detangled.arrowsFor(id)
    val arrowsList = buildArrowsList(listTemplate, elementTemplate, arrows)
    template.body().appendChild(arrowsList)
  }

  private def addNestedArrows(template: Document, listTemplate: Element, elementTemplate: Element, arrows: Seq[Arrow]) = {
    val arrowsList = buildArrowsList(listTemplate, elementTemplate, arrows)
    template.body().appendChild(arrowsList)
  }

  private def buildArrowsList(listTemplateOriginal: Element, elementTemplate: Element, arrows: Seq[Arrow]): Element = {
    val arrowElements = arrows.map(arrow => buildArrowElement(listTemplateOriginal, elementTemplate, arrow))
    val listTemplate = listTemplateOriginal.clone()
    arrowElements.foreach(arrowElement => listTemplate.appendChild(arrowElement))
    listTemplate
  }

  private def buildArrowElement(listTemplate: Element, elementTemplateOriginal: Element, arrow: Arrow): Element = {
    val elementTemplate = elementTemplateOriginal.clone()
    jsoupUtil.setText(elementTemplate, "content", HtmlUtil.arrowId(arrow.from, arrow.to))
    elementTemplate.appendChild(buildArrowsList(listTemplate, elementTemplateOriginal, arrow.reasons))
    elementTemplate
  }

  //  private def appendReasons(target: Element, context:UnitId, unitId:UnitId, reasonsOriginal:Element, reasonOriginal:Element): Unit = {
  //    val arrows = detangled.arrowsFor(unitId)
  //    appendArrows(target, context, arrows, reasonsOriginal, reasonOriginal)
  //  }
  //
  //  private def appendArrows(target: Element, context:UnitId, arrows:Seq[Arrow], reasonsOriginal:Element, reasonOriginal:Element): Unit = {
  //    val reasons = reasonsOriginal.clone()
  //    arrows.foreach(arrow => appendArrow(reasons, context, arrow, reasonOriginal, reasonOriginal))
  //    target.appendChild(reasons)
  //  }
  //
  //  private def appendArrow(target: Element, context:UnitId, arrow:Arrow, reasonsOriginal:Element, reasonOriginal:Element): Unit = {
  //    val reason = reasonOriginal.clone()
  //    val arrowFromLink = HtmlUtil.htmlLink(context, arrow.from)
  //    val arrowFromName = HtmlUtil.htmlName(arrow.from)
  //    val arrowToLink = HtmlUtil.htmlLink(context, arrow.to)
  //    val arrowToName = HtmlUtil.htmlName(arrow.to)
  //    JsoupUtil.setAnchor(reason, "from", arrowFromName, arrowFromLink, removeClasses)
  //    JsoupUtil.setAnchor(reason, "to", arrowToName, arrowToLink, removeClasses)
  //    appendArrows(reason, context, arrow.reasons, reasonsOriginal, reasonOriginal)
  //    target.appendChild(reason)
  //  }

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
