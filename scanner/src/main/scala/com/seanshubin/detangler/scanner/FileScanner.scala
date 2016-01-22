package com.seanshubin.detangler.scanner

import java.nio.file.Path

trait FileScanner {
  def loadBytes(jarOrDirectory: Path): Iterable[ScannedBytes]
}
