package com.seanshubin.detangler.scanner

class ClassBytesScannerImpl extends ClassBytesScanner {
  override def parseDependencies(classBytes: Seq[Byte]): (Seq[String], Seq[Seq[String]]) = ???
}
