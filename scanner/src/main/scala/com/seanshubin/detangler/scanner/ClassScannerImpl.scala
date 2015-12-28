package com.seanshubin.detangler.scanner

class ClassScannerImpl extends ClassScanner {
  override def parseDependencies(classBytes: Seq[Byte]): (Seq[String], Seq[Seq[String]]) = ???
}
