package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.DevonMarshallerWiring

trait PrototypeWiring {
  def dirName: String

  lazy val reportDir = Paths.get("generated", "reports", dirName)
  lazy val files = FilesDelegate
  lazy val reportTransformer = new ReportTransformerImpl
  lazy val classLoader = this.getClass.getClassLoader
  lazy val classLoaderIntegration = new ClassLoaderIntegrationImpl(classLoader)
  lazy val resourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
  lazy val detangled: Detangled = SampleData.detangled
  lazy val shouldRemoveClass: Boolean = true
  lazy val pageGenerator = new PageGeneratorImpl(detangled, resourceLoader, shouldRemoveClass)
  lazy val reporter = new ReporterImpl(
    reportDir,
    files,
    DevonMarshallerWiring.Default,
    StandardCharsets.UTF_8,
    reportTransformer,
    pageGenerator,
    resourceLoader,
    detangled
  )

}
