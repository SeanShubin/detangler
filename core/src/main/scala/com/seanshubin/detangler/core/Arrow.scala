package com.seanshubin.detangler.core

case class Arrow(from: UnitId, to: UnitId, reasons: Seq[Arrow])

object Arrow {
  def toIndentedLines(arrow:Arrow):Seq[String] = {
    toIndentedLinesFrom(arrow, 0)
  }

  private def toIndentedLinesFrom(arrow:Arrow, depth:Int):Seq[String] = {
    val Arrow(from, to, reasons) = arrow
    val prefix = indent(depth)
    val head:String = s"$prefix${from.name} -> ${to.name}"
    def nextLevelDeep = toIndentedLinesFrom(_:Arrow, depth + 1)
    val tail:Seq[String] = reasons.flatMap(nextLevelDeep)
    head +: tail
  }

  private def indent(depth:Int):String = "  " * depth
}
