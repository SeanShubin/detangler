package com.seanshubin.detangler.report

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.detangler.model.{Cycle, Module, Standalone}

object HtmlRendering {
  private val FileSystemCharacters = "/\\?%*:|\"<>. "
  private val CssSelectorCharacters = "~!@$%^&*()+=,./';:\"?><[]\\{}|`#"

  def summaryFileName: String = "index.html"

  def reportFile(standalone: Standalone): String = {
    "report-" + baseFileName(standalone) + ".html"
  }

  def htmlName(standalone: Standalone): String = {
    standalone.path.last
  }

  def htmlLink(context: Standalone, standalone: Standalone): String = {
    if (standalone.path.init == context.path) {
      innerHtmlLinkFor(standalone)
    } else {
      outerHtmlLinkFor(standalone)
    }
  }

  def graphLink(context: Standalone): String = {
    "graph-" + baseFileName(context) + ".html"
  }

  def graphSourceFile(context: Standalone): String = {
    "graph-" + baseFileName(context) + ".txt"
  }

  private def baseFileName(standalone: Standalone): String = {
    standalone.path.mkString("--").map(makeFileSystemSafe)
  }

  private def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '-'
    else c
  }

  def graphTargetFile(context: Standalone): String = {
    "graph-" + baseFileName(context) + ".svg"
  }

  def graphText(context: Standalone): String = {
    "graph"
  }

  def cycleLink(cycle: Cycle): String = {
    "#" + cycleId(cycle)
  }

  def cycleId(cycle: Cycle): String = {
    "cycle-" + htmlId(firstInCycle(cycle))
  }

  def htmlId(standalone: Standalone): String = {
    standalone.path.last.map(makeCssSelectorSafe)
  }

  private def makeCssSelectorSafe(c: Char): Char = {
    if (CssSelectorCharacters.contains(c)) '-'
    else c
  }

  private def firstInCycle(cycle: Cycle): Standalone = {
    cycle.parts.toSeq.sortWith(Compare.lessThan(Standalone.compare)).head
  }

  def innerHtmlLinkFor(standalone: Standalone): String = {
    "#" + htmlId(standalone)
  }

  def parentLink(standalone: Standalone): String = {
    if (standalone.isRoot) {
      "index.html"
    } else {
      outerHtmlLinkFor(standalone)
    }
  }

  def parentName(standalone: Standalone): String = {
    if (standalone.isRoot) {
      "back to summary"
    } else {
      htmlName(standalone)
    }
  }

  def outerHtmlLinkFor(module: Module): String = {
    module match {
      case standalone: Standalone =>
        reportFile(Standalone(standalone.path.init)) + "#" + htmlId(standalone)
      case cycle: Cycle =>
        reportFile(Standalone(cycle.standalone.path.init)) + "#cycle-" + htmlId(cycle.standalone)
    }
  }

  def reasonLink(from: Standalone, to: Standalone): String = {
    "#" + reasonId(from, to)
  }

  def reasonId(from: Standalone, to: Standalone): String = {
    qualifiedHtmlId(from) + "---" + qualifiedHtmlId(to)
  }

  def qualifiedHtmlId(standalone: Standalone): String = {
    standalone.path.mkString("--").map(makeCssSelectorSafe)
  }

  def reasonName(from: Standalone, to: Standalone): String = "reason"
}
