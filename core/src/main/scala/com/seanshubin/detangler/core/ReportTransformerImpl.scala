package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html._

class ReportTransformerImpl extends ReportTransformer {
  override def pageFor(detangled: Detangled, unitId: UnitId): HtmlPage = {
    new RootTransformerDelegate(detangled).pageFor(unitId)
  }

  override def htmlId(unitId: UnitId): String = {
    unitId.paths.map(htmlIdForSetOfString).mkString("--")
  }

  override def htmlName(unitId: UnitId): String = {
    unitId.paths.last.toSeq.sorted.mkString("-")
  }

  override def htmlLinkAbsolute(unitId: UnitId): String = {
    val path = htmlFileName(unitId)
    val fragment = htmlLinkRelative(unitId)
    s"$path$fragment"
  }

  override def htmlLinkRelative(unitId: UnitId): String = {
    val fragment = htmlId(unitId)
    s"#$fragment"
  }

  override def htmlFileName(unitId: UnitId): String = {
    val htmlFileName = if (unitId.paths.size == 0) {
      "index.html"
//    } else if(unitId.paths.size == 1) {
//      "index.html"
    } else {
      unitId.paths.init.map(htmlIdForSetOfString).mkString("--").map(makeFileSystemSafe) + ".html"
    }
    htmlFileName
  }

  override def arrowId(from: UnitId, to: UnitId): String = {
    htmlId(from) + "---" + htmlId(to)
  }

  override def arrowName(from: UnitId, to: UnitId): String = "reason"

  private def parentAndUnitIdToAnchor(parent:UnitId, unitId: UnitId): HtmlAnchor = {
    val name = htmlName(unitId)
    val link = if(shouldUseRelativeLink(parent, unitId)) {
      htmlLinkRelative(unitId)
    } else {
      htmlLinkAbsolute(unitId)
    }
    HtmlAnchor(name, link)
  }

  private def shouldUseRelativeLink(parent:UnitId, child:UnitId):Boolean = {
    val exactlyOneLevelLower = parent.paths.size == child.paths.size - 1
    val isChild = child.paths.startsWith(parent.paths)
    exactlyOneLevelLower && isChild
  }

  private def arrowAnchor(from: UnitId, to: UnitId): HtmlAnchor = {
    val name = arrowName(from, to)
    val link = "index.html#" + arrowId(from, to)
    val anchor = HtmlAnchor(name, link)
    anchor
  }

  private def partsAnchorFor(unitId: UnitId): HtmlAnchor = {
    val name = "parts"
    val link = htmlId(unitId).map(makeFileSystemSafe) + ".html"
    HtmlAnchor(name, link)
  }

  private def htmlIdForSetOfString(setOfString: Set[String]): String = {
    setOfString.toSeq.sorted.mkString("-")
  }

  val FileSystemCharacters = "/\\?%*:|\"<>. "

  def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '_'
    else c
  }

  private class RootTransformerDelegate(detangled: Detangled) {
    def pageFor(unitId: UnitId): HtmlPage = {
      val fileName = fileNameFor(unitId)
      val units = unitsFor(unitId, unitId)
      val reasons = reasonsFor(unitId)
      HtmlPage(fileName, units, reasons)
    }

    def fileNameFor(unitId:UnitId): String = htmlFileName(unitId)

    def unitsFor(parent:UnitId, unitId: UnitId): Seq[HtmlUnit] = {
      def unitIdToHtmlUnit(x:UnitId) = parentAndUnitIdToHtmlUnit(parent, x)
      detangled.map(unitId).composedOf.toSeq.sorted.map(unitIdToHtmlUnit)
    }

    def reasonsFor(unitId: UnitId): Seq[HtmlReason] = {
      def arrowToHtmlReason(arrow:Arrow) = unitIdAndArrowToHtmlReason(unitId, arrow)
      val topLevelArrows = detangled.arrowsFor(unitId)
      topLevelArrows.map(arrowToHtmlReason)
    }

    private def unitIdAndArrowToHtmlReason(unitId:UnitId, arrow: Arrow): HtmlReason = {
      def arrowToHtmlReason(arrow:Arrow) = unitIdAndArrowToHtmlReason(unitId, arrow)
      val from = parentAndUnitIdToAnchor(unitId,arrow.from)
      val to = parentAndUnitIdToAnchor(unitId, arrow.to)
      val reasons = arrow.reasons.map(arrowToHtmlReason)
      HtmlReason(from, to, reasons)
    }

    def parentAndUnitIdToHtmlUnit(parent:UnitId, unitId: UnitId): HtmlUnit = {
      def toHtmlUnitLink(otherUnitId: UnitId) = relationToHtmlUnitLink(parent, unitId, otherUnitId)
      val unit = detangled.map(unitId)
      val id = htmlId(unitId)
      val name = id
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

    private def relationToHtmlUnitLink(parent:UnitId, from: UnitId, to: UnitId): HtmlUnitLink = {
      val unitInfo = detangled.map(to)
      val anchor: HtmlAnchor = parentAndUnitIdToAnchor(parent, to)
      val depth: String = unitInfo.depth.toString
      val complexity: String = unitInfo.complexity.toString
      val reasonAnchor: HtmlAnchor = arrowAnchor(from, to)
      HtmlUnitLink(anchor, depth, complexity, reasonAnchor)
    }
  }

}
