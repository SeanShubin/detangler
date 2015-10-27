package com.seanshubin.detangler.console

import java.nio.file.Path

import com.seanshubin.detangler.core._

trait AfterConfigurationWiring {
  def reportDir: Path

  lazy val reporterFactory: ReporterFactory = new ReporterFactoryImpl
  lazy val analyzer: Runnable = new AfterConfigurationRunnerImpl(reporterFactory, reportDir)
}
