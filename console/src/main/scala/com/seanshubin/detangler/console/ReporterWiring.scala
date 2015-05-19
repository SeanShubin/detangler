package com.seanshubin.detangler.console

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path

import com.seanshubin.detangler.core._
import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.utility.filesystem.{FileSystemIntegration, FileSystemIntegrationImpl}

trait ReporterWiring {
  def detangled: Detangled

  def reportDir: Path

  lazy val fileSystem: FileSystemIntegration = new FileSystemIntegrationImpl
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val reportTransformer: ReportTransformer = new ReportTransformerImpl
  lazy val classLoader: ClassLoader = this.getClass.getClassLoader
  lazy val classLoaderIntegration: ClassLoaderIntegration = new ClassLoaderIntegrationImpl(classLoader)
  lazy val resourceLoader: ResourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
  lazy val removeClasses:Boolean = true
  lazy val pageGenerator: PageGenerator = new PageGeneratorImpl(detangled, resourceLoader, removeClasses)
  lazy val reporter: Reporter = new ReporterImpl(
    reportDir,
    fileSystem,
    devonMarshaller,
    charset,
    reportTransformer,
    pageGenerator,
    resourceLoader,
    detangled)
}
