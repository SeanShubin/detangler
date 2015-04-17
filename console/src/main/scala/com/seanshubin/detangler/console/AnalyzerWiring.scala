package com.seanshubin.detangler.console

import java.nio.file.Path

import com.seanshubin.detangler.core._

trait AnalyzerWiring {
  def reportDir:Path
  lazy val reporterFactory:ReporterFactory = new ReporterFactoryImpl
  lazy val analyzer: Analyzer = new AnalyzerImpl(reporterFactory, reportDir)
}
