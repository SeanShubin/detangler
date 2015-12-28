package com.seanshubin.detangler.scanner

import java.nio.file.Path

class JarScannerImpl extends JarScanner {
  override def loadBytes(jarOrDirectory: Path): Iterable[Seq[Byte]] = ???
}
