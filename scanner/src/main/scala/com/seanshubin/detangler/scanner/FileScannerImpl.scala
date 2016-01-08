package com.seanshubin.detangler.scanner

import java.nio.file.Path

import com.seanshubin.detangler.timer.Timer

class FileScannerImpl(zipScanner: ZipScanner,
                      classScanner: ClassScanner,
                      timer: Timer) extends FileScanner {
  override def loadBytes(jarOrClass: Path): Iterable[Seq[Byte]] = {
    if (FileTypes.isCompressed(jarOrClass.toString)) {
      timer.measureTime(s"scan compressed file $jarOrClass") {
        zipScanner.loadBytes(jarOrClass)
      }
    } else {
      classScanner.loadBytes(jarOrClass)
    }
  }
}
