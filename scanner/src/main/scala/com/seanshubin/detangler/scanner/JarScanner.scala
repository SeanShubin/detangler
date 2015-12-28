package com.seanshubin.detangler.scanner

import java.nio.file.Path

trait JarScanner {
  def loadBytes(jarOrDirectory: Path): Iterable[Seq[Byte]]
}
