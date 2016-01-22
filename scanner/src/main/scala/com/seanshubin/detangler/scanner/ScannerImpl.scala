package com.seanshubin.detangler.scanner

import java.nio.file.Path

import com.seanshubin.detangler.timer.Timer

class ScannerImpl(directoryScanner: DirectoryScanner,
                  fileScanner: FileScanner,
                  classBytesScanner: ClassBytesScanner,
                  timer: Timer) extends Scanner {
  override def scanDependencies(): Iterable[ScannedDependencies] = {
    val files: Iterable[Path] = directoryScanner.findFiles()
    val scannedBytesSeq:Iterable[ScannedBytes]  = files.flatMap(fileScanner.loadBytes)
    val scannedDependenciesSeq:Iterable[ScannedDependencies] = scannedBytesSeq.map(classBytesScanner.parseDependencies)
    scannedDependenciesSeq
  }
}
