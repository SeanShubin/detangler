package com.seanshubin.detangler.console

import com.seanshubin.detangler.core._

trait AnalyzerWiring {
  def configuration: Configuration

  lazy val emitLine: String => Unit = println
  lazy val runner: Analyzer = new AnalyzerImpl(configuration.greetingTarget, emitLine)
}
