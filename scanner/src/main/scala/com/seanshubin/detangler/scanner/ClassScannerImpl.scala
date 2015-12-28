package com.seanshubin.detangler.scanner

import java.nio.file.Path

import com.seanshubin.detangler.contract.FilesContract

class ClassScannerImpl(files: FilesContract) extends ClassScanner {
  override def loadBytes(path: Path): Iterable[Seq[Byte]] = Seq(files.readAllBytes(path))
}
