package com.seanshubin.detangler.core

object HtmlUtil {
  def fileNameFor(unitId: UnitId): String = {
    if (unitId.isRoot) {
      "index.html"
    } else {
      unitId.paths.map(htmlIdForSetOfString).mkString("--").map(makeFileSystemSafe) + ".html"
    }
  }

  def htmlId(unitId: UnitId): String = {
    unitId.paths.map(htmlIdForSetOfString).mkString("--").map(makeCssSelectorSafe)
  }

  def htmlName(unitId: UnitId): String = {
    unitId.paths.last.toSeq.sorted.mkString("-")
  }

  def htmlLink(context: UnitId, unitId: UnitId): String = {
    if (unitId.parent == context) {
      "#" + htmlId(unitId)
    } else {
      fileNameFor(unitId.parent) + "#" + htmlId(unitId)
    }
  }

  private val FileSystemCharacters = "/\\?%*:|\"<>. "
  private val CssSelectorCharacters = "~!@$%^&*()+=,./';:\"?><[]\\{}|`#"

  private def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '_'
    else c
  }

  private def makeCssSelectorSafe(c: Char): Char = {
    if (CssSelectorCharacters.contains(c)) '_'
    else c
  }

  private def htmlIdForSetOfString(setOfString: Set[String]): String = {
    setOfString.toSeq.sorted.mkString("-")
  }
}
