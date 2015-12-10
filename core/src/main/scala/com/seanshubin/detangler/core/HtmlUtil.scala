package com.seanshubin.detangler.core

object HtmlUtil {
  def fileNameFor(module: Module): String = {
    if (module.isRoot) {
      "index.html"
    } else {
      module.paths.map(htmlIdForSetOfString).mkString("--").map(makeFileSystemSafe) + ".html"
    }
  }

  def htmlId(module: Module): String = {
    module.paths.map(htmlIdForSetOfString).mkString("--").map(makeCssSelectorSafe)
  }

  def htmlName(module: Module): String = {
    module.paths.last.toSeq.sorted.mkString("-")
  }

  def htmlLink(context: Module, module: Module): String = {
    if (module.parent == context) {
      "#" + htmlId(module)
    } else {
      fileNameFor(module.parent) + "#" + htmlId(module)
    }
  }

  def reasonId(from: Module, to: Module): String = {
    htmlId(from) + "---" + HtmlUtil.htmlId(to)
  }

  def reasonLink(from: Module, to: Module): String = {
    "#" + reasonId(from, to)
  }

  def reasonName(from: Module, to: Module): String = "reason"


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
