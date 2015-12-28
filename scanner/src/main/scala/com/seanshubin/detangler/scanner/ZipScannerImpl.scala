package com.seanshubin.detangler.scanner

import java.nio.file.Path

class ZipScannerImpl extends ZipScanner {
  override def loadBytes(path: Path): Iterable[Seq[Byte]] = ???
}
