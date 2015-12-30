package com.seanshubin.detangler.scanner

import java.nio.file.Path

class FileScannerImpl(zipScanner: ZipScanner, classScanner: ClassScanner) extends FileScanner {
  override def loadBytes(jarOrClass: Path): Iterable[Seq[Byte]] = {
    if (FileTypes.isCompressed(jarOrClass.toString)) {
      zipScanner.loadBytes(jarOrClass)
    } else {
      classScanner.loadBytes(jarOrClass)
    }
  }
}
