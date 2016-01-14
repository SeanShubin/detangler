package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Module, Standalone}

object HtmlRender {
  private val FileSystemCharacters = "/\\?%*:|\"<>. "
  private val CssSelectorCharacters = "~!@$%^&*()+=,./';:\"?><[]\\{}|`#"

  def id(module: Module): String = {
    module match {
      case standalone: Standalone => standalone.path.last.map(makeCssSelectorSafe)
      case cycle: Cycle => "cycle-" + id(cycle.standalone)
    }
  }

  def navigateHigherLink(standalone: Standalone): String = {
    if (standalone.isRoot) {
      "index.html"
    } else {
      makeReportFileName(standalone.parent)
    }
  }

  def navigateHigherLinkName(standalone: Standalone): String = {
    if (standalone.isRoot) {
      "back to summary"
    } else {
      standalone.path.last
    }
  }

  def reportPageLink(standalone: Standalone): String = {
    makeReportFileName(standalone)
  }

  def reportPageLinkName(standalone: Standalone): String = {
    if (standalone.isRoot) {
      "-root-"
    } else {
      standalone.path.last
    }
  }

  def moduleLink(context: Standalone, module: Module): String = {
    if (context == module.parent) {
      relativeModuleLink(module)
    } else {
      absoluteModuleLink(module)
    }
  }

  def moduleLinkName(module: Module): String = {
    module match {
      case standalone: Standalone => standalone.path.last
      case cycle: Cycle => s"${cycle.parts.size} part cycle"
    }
  }

  def standaloneLinkQualifiedName(standalone: Standalone): String = {
    if (standalone.isRoot) {
      "-root-"
    } else {
      standalone.path.mkString("/")
    }
  }

  def absoluteModuleLink(module: Module): String = {
    val fileName = makeReportFileName(module.parent)
    val relativeLink = relativeModuleLink(module)
    fileName + relativeLink
  }

  def relativeModuleLink(module: Module): String = {
    "#" + id(module)
  }

  def graphLink(standalone: Standalone): String = {
    makeFileName("graph", standalone, ".html")
  }

  def graphLinkName(standalone: Standalone): String = {
    "graph"
  }

  def graphSourceLink(standalone: Standalone): String = {
    makeFileName("graph", standalone, ".txt")
  }

  def graphSourceLinkName(standalone: Standalone): String = {
    "graph source"
  }

  def graphTargetLink(standalone: Standalone): String = {
    makeFileName("graph", standalone, ".svg")
  }

  def graphTargetLinkName(standalone: Standalone): String = {
    "graph image"
  }

  def reasonId(from: Standalone, to: Standalone): String = {
    qualifiedHtmlId(from) + "---" + qualifiedHtmlId(to)
  }

  def reasonLink(from: Standalone, to: Standalone): String = "#" + reasonId(from, to)

  def reasonLinkName(from: Standalone, to: Standalone): String = "reason"

  def composedOfLink(standalone: Standalone): String = ???

  def composedOfLinkName(standalone: Standalone): String = ???

  def cycleLink(module: Module): String = ???

  def cycleLinkName(context: Standalone, module: Module): String = ???

  private def qualifiedHtmlId(standalone: Standalone): String = {
    standalone.path.mkString("--").map(makeCssSelectorSafe)
  }

  private def makeReportFileName(standalone: Standalone): String = {
    makeFileName("report", standalone, ".html")
  }

  private def makeFileName(prefix: String, standalone: Standalone, suffix: String): String = {
    (Seq(prefix) ++ standalone.path.map(makeFileSystemSafe)).mkString("--") + suffix
  }

  private def makeFileSystemSafe(s: String): String = {
    s.map(makeFileSystemSafe)
  }

  private def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '-'
    else c
  }

  private def makeCssSelectorSafe(c: Char): Char = {
    if (CssSelectorCharacters.contains(c)) '-'
    else c
  }
}
