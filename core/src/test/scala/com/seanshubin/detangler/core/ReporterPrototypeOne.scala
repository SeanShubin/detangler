package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.DefaultDevonMarshaller
import com.seanshubin.utility.filesystem.FileSystemIntegrationImpl

object ReporterPrototypeOne extends App {
  val reporter = new ReporterImpl(
    reportDir = Paths.get("generated", "reports", "1"),
    fileSystem = new FileSystemIntegrationImpl,
    devonMarshaller = DefaultDevonMarshaller,
    StandardCharsets.UTF_8,
    new ReportTransformerImpl,
    new PageGeneratorImpl
  )

  reporter.generateReportsOne(SampleData.detangled)
}
