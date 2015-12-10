package com.seanshubin.detangler.core

case class Reason(from: Module, to: Module, reasons: Seq[Reason])

object Reason {
  def toIndentedLines(reason: Reason): Seq[String] = {
    toIndentedLinesFrom(reason, 0)
  }

  private def toIndentedLinesFrom(reason: Reason, depth: Int): Seq[String] = {
    val Reason(from, to, reasons) = reason
    val prefix = indent(depth)
    val head: String = s"$prefix${from.name} -> ${to.name}"
    def nextLevelDeep = toIndentedLinesFrom(_: Reason, depth + 1)
    val tail: Seq[String] = reasons.flatMap(nextLevelDeep)
    head +: tail
  }

  private def indent(depth: Int): String = "  " * depth
}
