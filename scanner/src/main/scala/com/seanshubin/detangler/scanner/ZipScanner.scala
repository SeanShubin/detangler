package com.seanshubin.detangler.scanner

import java.nio.file.Path

trait ZipScanner {
  def loadBytes(path: Path): Iterable[Seq[Byte]]
}
