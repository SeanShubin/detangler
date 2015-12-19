package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

object HtmlUtil {
  def fileNameFor(standalone: Standalone): String = {
    if (standalone.path.isEmpty) {
      "index.html"
    } else {
      standalone.path.mkString("--").map(makeFileSystemSafe) + ".html"
    }
  }

  def htmlId(standalone: Standalone): String = {
    standalone.path.last.map(makeCssSelectorSafe)
  }

  def qualifiedHtmlId(standalone: Standalone): String = {
    standalone.path.mkString("--").map(makeCssSelectorSafe)
  }

  def htmlName(standalone: Standalone): String = {
    standalone.path.last
  }

  def htmlLink(context: Standalone, standalone: Standalone): String = {
    if (standalone.path.init == context.path) {
      "#" + htmlId(standalone)
    } else {
      fileNameFor(Standalone(standalone.path.init)) + "#" + htmlId(standalone)
    }
  }

  def reasonId(from: Standalone, to: Standalone): String = {
    qualifiedHtmlId(from) + "---" + qualifiedHtmlId(to)
  }

  def reasonLink(from: Standalone, to: Standalone): String = {
    "#" + reasonId(from, to)
  }

  def reasonName(from: Standalone, to: Standalone): String = "reason"

  private val FileSystemCharacters = "/\\?%*:|\"<>. "
  private val CssSelectorCharacters = "~!@$%^&*()+=,./';:\"?><[]\\{}|`#"

  private def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '-'
    else c
  }

  private def makeCssSelectorSafe(c: Char): Char = {
    if (CssSelectorCharacters.contains(c)) '-'
    else c
  }
}
