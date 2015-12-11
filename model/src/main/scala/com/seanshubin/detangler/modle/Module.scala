package com.seanshubin.detangler.modle

case class Module(parts: Seq[String]) {
  def id: String = parts.mkString("-")
}
