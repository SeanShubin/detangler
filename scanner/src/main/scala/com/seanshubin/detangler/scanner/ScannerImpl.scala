package com.seanshubin.detangler.scanner

import java.nio.file.Path

import com.seanshubin.detangler.timer.Timer

class ScannerImpl(directoryScanner: DirectoryScanner,
                  fileScanner: FileScanner,
                  classBytesScanner: ClassBytesScanner,
                  timer: Timer) extends Scanner {
  override def scanDependencies(): Iterable[(String, Seq[String])] = {
    val files: Iterable[Path] = directoryScanner.findFiles()
    def scanFile(file: Path): Iterable[Seq[Byte]] = timer.measureTime(s"scan file $file") {
      fileScanner.loadBytes(file)
    }
    val classBytesSeq = timer.measureTime("load bytes")(files.flatMap(scanFile))
    val dependencies = timer.measureTime("parse classes")(classBytesSeq.map(classBytesScanner.parseDependencies))
    dependencies
  }
}
