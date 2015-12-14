package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

object HtmlUtil {
  def fileNameFor(single: Single): String = {
    if (single.path.isEmpty) {
      "index.html"
    } else {
      single.path.mkString("--").map(makeFileSystemSafe) + ".html"
    }
  }

  def htmlId(single: Single): String = {
    single.path.last.map(makeCssSelectorSafe)
  }

  def htmlName(single: Single): String = {
    single.path.last
  }

  def htmlLink(context: Single, single: Single): String = {
    if (single.path.init == context.path) {
      "#" + htmlId(single)
    } else {
      fileNameFor(Single(single.path.init)) + "#" + htmlId(single)
    }
  }

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
