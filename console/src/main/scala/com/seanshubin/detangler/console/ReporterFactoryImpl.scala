package com.seanshubin.detangler.console

import java.nio.file.Path

import com.seanshubin.detangler.core._

class ReporterFactoryImpl extends ReporterFactory {
  override def createReporter(theDetangled:Detangled, theReportDir:Path): Reporter = {
    new ReporterWiring {
      override def detangled: Detangled = theDetangled

      override def reportDir: Path = theReportDir
    }.reporter
  }
}
