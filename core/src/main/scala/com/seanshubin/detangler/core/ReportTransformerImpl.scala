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
      detangled.map(unitId).composedOf.toSeq.sorted.map(unitIdToHtmlUnit)
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
      val unit = detangled.map(unitId)
      val id = HtmlUtil.htmlId(unitId)
      val name = HtmlUtil.htmlName(unitId)
      val depth = unit.depth.toString
      val complexity = unit.complexity.toString
      val partsAnchor = partsAnchorFor(unitId)
      val dependsOn = unit.dependsOn.toSeq.sorted.map(toHtmlUnitLink)
      val dependedOnBy = unit.dependedOnBy.toSeq.sorted.map(toHtmlUnitLink)
      val dependsOnExternal = unit.dependsOnExternal.toSeq.sorted.map(toHtmlUnitLink)
      val dependedOnByExternal = unit.dependedOnByExternal.toSeq.sorted.map(toHtmlUnitLink)
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

    def toUnitInfo(unitId: UnitId) = detangled.map(unitId)

    private def relationToHtmlUnitLink(parent: UnitId, from: UnitId, to: UnitId): HtmlUnitLink = {
      val unitInfo = detangled.map(to)
      val anchor: HtmlAnchor = parentAndUnitIdToAnchor(parent, to)
      val depth: String = unitInfo.depth.toString
      val complexity: String = unitInfo.complexity.toString
      val reasonAnchor: HtmlAnchor = arrowAnchor(from, to)
      HtmlUnitLink(anchor, depth, complexity, reasonAnchor)
    }
  }

}
