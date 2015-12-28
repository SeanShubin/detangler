package com.seanshubin.detangler.scanner

import java.nio.file.Path

class ScannerImpl(fileScanner: FileScanner, jarScanner: JarScanner, classScanner: ClassScanner) extends Scanner {
  override def scanDependencies(): Iterable[(Seq[String], Seq[Seq[String]])] = {
    val files: Iterable[Path] = fileScanner.findFiles()
    val classBytesSeq: Iterable[Seq[Byte]] = files.flatMap(jarScanner.loadBytes)
    val dependencies: Iterable[(Seq[String], Seq[Seq[String]])] = classBytesSeq.map(classScanner.parseDependencies)
    dependencies
  }
}
