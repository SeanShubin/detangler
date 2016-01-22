package com.seanshubin.detangler.scanner

trait ClassBytesScanner {
  def parseDependencies(scannedBytes:ScannedBytes): ScannedDependencies
}
