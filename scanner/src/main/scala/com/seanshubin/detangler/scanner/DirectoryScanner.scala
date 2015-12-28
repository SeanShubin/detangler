package com.seanshubin.detangler.scanner

import java.nio.file.Path

trait DirectoryScanner {
  def findFiles(): Iterable[Path]
}
