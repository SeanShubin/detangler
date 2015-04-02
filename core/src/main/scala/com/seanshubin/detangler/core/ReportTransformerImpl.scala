package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html._

class ReportTransformerImpl extends ReportTransformer {
  override def rootReport(detangled: Detangled): HtmlPage = {
    new RootTransformerDelegate(detangled).rootReport()
  }

  override def htmlId(unitId: UnitId): String = {
    unitId.paths.map(htmlIdForSetOfString).mkString("--")
  }

  override def htmlName(unitId: UnitId): String = {
    unitId.paths.last.toSeq.sorted.mkString("-")
  }

  override def htmlLink(unitId: UnitId): String = {
    val path = htmlFileName(unitId)
    val fragment = htmlId(unitId)
    s"$path#$fragment"
  }

  override def htmlFileName(unitId: UnitId): String = {
    val htmlFileName = if (unitId.paths.size == 1) {
      "index.html"
    } else {
      unitId.paths.init.map(htmlIdForSetOfString).mkString("--").map(makeFileSystemSafe) + ".html"
    }
    htmlFileName
  }

  override def arrowId(from: UnitId, to: UnitId): String = {
    htmlId(from) + "---" + htmlId(to)
  }

  override def arrowName(from: UnitId, to: UnitId): String = "reason"

  private def unitIdToAnchor(unitId: UnitId): HtmlAnchor = {
    val name = htmlName(unitId)
    val link = htmlLink(unitId)
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
    val link = htmlId(unitId).map(makeFileSystemSafe)
    HtmlAnchor(name, link)
  }

  private def htmlIdForSetOfString(setOfString: Set[String]): String = {
    setOfString.toSeq.sorted.mkString("-")
  }

  private def unitInfoToHtmlUnitLink(unitInfo: UnitInfo): HtmlUnitLink = {
    val name = ""
    val link = ""
    val anchor: HtmlAnchor = HtmlAnchor(name, link)
    val depth: String = unitInfo.depth.toString
    val complexity: String = unitInfo.complexity.toString
    val reasonName = ""
    val reasonLink = ""
    val reasonAnchor: HtmlAnchor = HtmlAnchor(reasonName, reasonLink)
    HtmlUnitLink(anchor, depth, complexity, reasonAnchor)
  }

  val FileSystemCharacters = "/\\?%*:|\"<>. "

  def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '_'
    else c
  }

  private class RootTransformerDelegate(detangled: Detangled) {
    def rootReport(): HtmlPage = {
      val fileName = rootFileName()
      val units = rootUnits()
      val reasons = rootReasons()
      HtmlPage(fileName, units, reasons)
    }

    def rootFileName(): String = "index.html"

    def rootUnits(): Seq[HtmlUnit] = {
      detangled.topLevelUnits().toSeq.sorted.map(rootUnit)
    }

    def rootReasons(): Seq[HtmlReason] = {
      val topLevelArrows = detangled.topLevelArrows()
      topLevelArrows.map(arrowToHtmlReason)
    }

    private def arrowToHtmlReason(arrow: Arrow): HtmlReason = {
      val from = unitIdToAnchor(arrow.from)
      val to = unitIdToAnchor(arrow.to)
      val reasons = arrow.reasons.map(arrowToHtmlReason)
      HtmlReason(from, to, reasons)
    }

    def rootUnit(unitId: UnitId): HtmlUnit = {
      def toHtmlUnitLink(otherUnitId: UnitId) = relationToHtmlUnitLink(unitId, otherUnitId)
      val unit = detangled.map(unitId)
      val id = htmlId(unitId)
      val name = id
      val depth = unit.depth.toString
      val complexity = unit.complexity.toString
      val partsAnchor = partsAnchorFor(unitId)
      val dependsOn = unit.dependsOn.toSeq.sorted.map(detangled.map).map(unitInfoToHtmlUnitLink)
      val dependedOnBy = unit.dependedOnBy.toSeq.sorted.map(detangled.map).map(unitInfoToHtmlUnitLink)
      val dependsOnExternal = unit.dependsOnExternal.toSeq.sorted.map(detangled.map).map(unitInfoToHtmlUnitLink)
      val dependedOnByExternal = unit.dependedOnByExternal.toSeq.sorted.map(detangled.map).map(unitInfoToHtmlUnitLink)
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

    private def relationToHtmlUnitLink(from: UnitId, to: UnitId): HtmlUnitLink = {
      val unitInfo = detangled.map(to)
      val anchorName = htmlName(to)
      val anchorLink = htmlLink(to)
      val anchor: HtmlAnchor = HtmlAnchor(anchorName, anchorLink)
      val depth: String = unitInfo.depth.toString
      val complexity: String = unitInfo.complexity.toString
      val reasonAnchor: HtmlAnchor = arrowAnchor(from, to)
      HtmlUnitLink(anchor, depth, complexity, reasonAnchor)
    }
  }

}
