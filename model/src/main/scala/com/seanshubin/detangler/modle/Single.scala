package com.seanshubin.detangler.modle

case class Single(path: Seq[String]) extends Module {
  override def toString: String = s"Single(${path.mkString("-")})"
}
