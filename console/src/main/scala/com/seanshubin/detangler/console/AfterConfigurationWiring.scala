package com.seanshubin.detangler.console

import java.nio.file.Path

import com.seanshubin.detangler.core._

trait AfterConfigurationWiring {
  def reportDir: Path

  lazy val createReporter: (Detangled, Path) => Runnable = (theDetangled, theReportDir) => new ReporterWiring {
    override def detangled: Detangled = theDetangled

    override def reportDir: Path = theReportDir
  }.reporter

  lazy val analyzer: Runnable = new AfterConfigurationRunnerImpl(createReporter, reportDir)
}
