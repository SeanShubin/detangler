package com.seanshubin.detangler.scanner

trait ClassScanner {
  def parseDependencies(classBytes: Seq[Byte]): (Seq[String], Seq[Seq[String]])
}
