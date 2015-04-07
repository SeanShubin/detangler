package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html._

class ReportTransformerImpl extends ReportTransformer {
  override def pageFor(detangled: Detangled, unitId: UnitId): HtmlPage = {
    new RootTransformerDelegate(detangled).pageFor(unitId)
  }

  override def arrowId(from: UnitId, to: UnitId): String = {
    HtmlUtil.htmlId(from) + "---" + HtmlUtil.htmlId(to)
  }

  override def arrowName(from: UnitId, to: UnitId): String = "reason"

  private def parentAndUnitIdToAnchor(parent: UnitId, unitId: UnitId): HtmlAnchor = {
    val name = HtmlUtil.htmlName(unitId)
    val link = HtmlUtil.htmlLink(parent, unitId)
    HtmlAnchor(name, link)
  }

  private def arrowAnchor(from: UnitId, to: UnitId): HtmlAnchor = {
    val name = arrowName(from, to)
    val link = "#" + arrowId(from, to)
    val anchor = HtmlAnchor(name, link)
    anchor
  }

  private def partsAnchorFor(unitId: UnitId): HtmlAnchor = {
    val name = "parts"
    val link = HtmlUtil.fileNameFor(unitId)
    HtmlAnchor(name, link)
  }

  private class RootTransformerDelegate(detangled: Detangled) {
    def pageFor(unitId: UnitId): HtmlPage = {
      val fileName = HtmlUtil.fileNameFor(unitId)
      val units = unitsFor(unitId, unitId)
      val reasons = reasonsFor(unitId)
      HtmlPage(fileName, units, reasons)
    }

    def unitsFor(parent: UnitId, unitId: UnitId): Seq[HtmlUnit] = {
      def unitIdToHtmlUnit(x: UnitId) = parentAndUnitIdToHtmlUnit(parent, x)
      detangled.composedOf(unitId).map(unitIdToHtmlUnit)
    }

    def reasonsFor(unitId: UnitId): Seq[HtmlReason] = {
      def arrowToHtmlReason(arrow: Arrow) = unitIdAndArrowToHtmlReason(unitId, arrow)
      val topLevelArrows = detangled.arrowsFor(unitId)
      topLevelArrows.map(arrowToHtmlReason)
    }

    private def unitIdAndArrowToHtmlReason(unitId: UnitId, arrow: Arrow): HtmlReason = {
      def arrowToHtmlReason(arrow: Arrow) = unitIdAndArrowToHtmlReason(unitId, arrow)
      val from = parentAndUnitIdToAnchor(unitId, arrow.from)
      val to = parentAndUnitIdToAnchor(unitId, arrow.to)
      val reasons = arrow.reasons.map(arrowToHtmlReason)
      HtmlReason(from, to, reasons)
    }

    def parentAndUnitIdToHtmlUnit(parent: UnitId, unitId: UnitId): HtmlUnit = {
      def toHtmlUnitLink(otherUnitId: UnitId) = relationToHtmlUnitLink(parent, unitId, otherUnitId)
      val id = HtmlUtil.htmlId(unitId)
      val name = HtmlUtil.htmlName(unitId)
      val depth = detangled.depth(unitId).toString
      val complexity = detangled.complexity(unitId).toString
      val partsAnchor = partsAnchorFor(unitId)
      val dependsOn = detangled.dependsOn(unitId).map(toHtmlUnitLink)
      val dependedOnBy = detangled.dependedOnBy(unitId).map(toHtmlUnitLink)
      val dependsOnExternal = detangled.dependsOnExternal(unitId).map(toHtmlUnitLink)
      val dependedOnByExternal = detangled.dependedOnByExternal(unitId).map(toHtmlUnitLink)
      HtmlUnit(id,
        name,
        depth,
        complexity,
        partsAnchor,
        dependsOn,
        dependedOnBy,
        dependsOnExternal,
        dependedOnByExternal)
    }

    private def relationToHtmlUnitLink(parent: UnitId, from: UnitId, to: UnitId): HtmlUnitLink = {
      val anchor: HtmlAnchor = parentAndUnitIdToAnchor(parent, to)
      val depth: String = detangled.depth(to).toString
      val complexity: String = detangled.complexity(to).toString
      val reasonAnchor: HtmlAnchor = arrowAnchor(from, to)
      HtmlUnitLink(anchor, depth, complexity, reasonAnchor)
    }
  }

}
