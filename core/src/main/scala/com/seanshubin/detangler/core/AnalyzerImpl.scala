package com.seanshubin.detangler.core

class AnalyzerImpl(target: String, emitLine: String => Unit) extends Analyzer {
  override def run(): Unit = emitLine(s"Hello, $target!")
}
