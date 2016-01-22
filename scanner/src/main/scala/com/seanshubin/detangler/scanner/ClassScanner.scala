package com.seanshubin.detangler.scanner

import java.nio.file.Path

trait ClassScanner {
  def loadBytes(path: Path): Iterable[ScannedBytes]
}
