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

  override def htmlAnchor(unitId: UnitId): String = {
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

  private def partsAnchorFor(unitId: UnitId): HtmlAnchor = {
    val name = "parts"
    val link = htmlId(unitId).map(makeFileSystemSafe)
    HtmlAnchor(name, link)
  }

  private def htmlIdForSetOfString(setOfString: Set[String]): String = {
    setOfString.toSeq.sorted.mkString("-")
  }

  private def toHtmlUnitLink(unitId: UnitId): HtmlUnitLink = {
    val anchor: HtmlAnchor = ???
    val depth: String = ???
    val complexity: String = ???
    val reasonAnchor: HtmlAnchor = ???
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

    def rootReasons(): Seq[HtmlReason] = ???

    def rootUnit(unitId: UnitId): HtmlUnit = {
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
  }

}
