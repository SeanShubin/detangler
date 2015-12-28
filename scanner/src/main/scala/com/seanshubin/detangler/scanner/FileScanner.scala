package com.seanshubin.detangler.scanner

import java.nio.file.Path

trait FileScanner {
  def findFiles(): Iterable[Path]
}
