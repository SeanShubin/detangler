package com.seanshubin.detangler.scanner

trait Scanner {
  def scanDependencies(): Iterable[ScannedDependencies]
}
