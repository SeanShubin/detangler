package com.seanshubin.detangler.scanner

import java.nio.file.Path

class ScannerImpl(directoryScanner: DirectoryScanner, fileScanner: FileScanner, classBytesScanner: ClassBytesScanner) extends Scanner {
  override def scanDependencies(): Iterable[(String, Seq[String])] = {
    val files: Iterable[Path] = directoryScanner.findFiles()
    val classBytesSeq: Iterable[Seq[Byte]] = files.flatMap(fileScanner.loadBytes)
    val dependencies: Iterable[(String, Seq[String])] = classBytesSeq.map(classBytesScanner.parseDependencies)
    dependencies
  }
}