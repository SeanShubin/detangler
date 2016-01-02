package com.seanshubin.detangler.report

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.detangler.model.{Cycle, Standalone}

object HtmlRendering {
  private val FileSystemCharacters = "/\\?%*:|\"<>. "
  private val CssSelectorCharacters = "~!@$%^&*()+=,./';:\"?><[]\\{}|`#"

  def fileNameFor(standalone: Standalone): String = {
    fileNameWithoutExtFor(standalone) + ".html"
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
    fileNameWithoutExtFor(context) + "-graph.html"
  }

  def graphFile(context: Standalone): String = {
    fileNameWithoutExtFor(context) + ".svg"
  }

  def graphSourceFile(context: Standalone): String = {
    fileNameWithoutExtFor(context) + ".txt"
  }

  def graphTargetFile(context: Standalone): String = {
    fileNameWithoutExtFor(context) + ".svg"
  }

  private def fileNameWithoutExtFor(standalone: Standalone): String = {
    if (standalone.path.isEmpty) {
      "index"
    } else {
      standalone.path.mkString("--").map(makeFileSystemSafe)
    }
  }

  private def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '-'
    else c
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

  def outerHtmlLinkFor(standalone: Standalone): String = {
    fileNameFor(Standalone(standalone.path.init)) + "#" + htmlId(standalone)
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
