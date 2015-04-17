package com.seanshubin.detangler.console

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.detangler.core._
import com.seanshubin.devon.core.devon.DevonMarshaller
import com.seanshubin.utility.filesystem.FileSystemIntegration

trait ReporterWiring {
  def detangled: Detangled

  lazy val reportDir: Path = ???
  lazy val fileSystem: FileSystemIntegration = ???
  lazy val devonMarshaller: DevonMarshaller = ???
  lazy val charset: Charset = ???
  lazy val reportTransformer: ReportTransformer = ???
  lazy val pageGenerator: PageGenerator = ???
  lazy val resourceLoader: ResourceLoader = ???
  lazy val reporter: Reporter = new ReporterImpl(
    reportDir,
    fileSystem,
    devonMarshaller,
    charset,
    reportTransformer,
    pageGenerator,
    resourceLoader)
}
