package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.DefaultDevonMarshaller
import com.seanshubin.utility.filesystem.FileSystemIntegrationImpl

object ReporterPrototype extends App {
  val reporter = new ReporterImpl(
    reportDir = Paths.get("generated", "reports"),
    fileSystem = new FileSystemIntegrationImpl,
    devonMarshaller = DefaultDevonMarshaller,
    StandardCharsets.UTF_8
  )

  reporter.generateReports(SampleData.detangled)
}
