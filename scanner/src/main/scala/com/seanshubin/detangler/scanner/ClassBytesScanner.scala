package com.seanshubin.detangler.scanner

trait ClassBytesScanner {
  def parseDependencies(classBytes: Seq[Byte]): (String, Seq[String])
}
