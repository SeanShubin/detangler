package com.seanshubin.detangler.scanner

trait ClassBytesScanner {
  def parseDependencies(classBytes: Seq[Byte]): (Seq[String], Seq[Seq[String]])
}
