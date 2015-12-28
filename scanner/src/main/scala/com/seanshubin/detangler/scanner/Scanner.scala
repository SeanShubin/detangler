package com.seanshubin.detangler.scanner

trait Scanner {
  def scanDependencies(): Iterable[(Seq[String], Seq[Seq[String]])]
}
