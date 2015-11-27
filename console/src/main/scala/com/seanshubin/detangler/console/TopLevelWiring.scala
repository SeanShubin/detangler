package com.seanshubin.detangler.console

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path

import com.seanshubin.detangler.core._
import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.utility.filesystem.{FileSystemIntegration, FileSystemIntegrationImpl}
import com.sun.tools.internal.jxc.SchemaGenerator.Runner

trait TopLevelWiring {
  def commandLineArguments: Seq[String]

  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystemIntegration = new FileSystemIntegrationImpl
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val notifications: Notifications = new LineEmittingNotifications(devonMarshaller, emitLine)
  lazy val configurationFactory: ConfigurationFactory = new ConfigurationFactoryImpl(
    fileSystem, devonMarshaller, charset)
  lazy val createRunner: Configuration => Runnable = (configuration) =>
    new AfterConfigurationWiring {
      override def reportDir: Path = configuration.reportDir
    }.analyzer

  lazy val launcher: Runnable = new TopLevelRunnerImpl(
    commandLineArguments, configurationFactory, createRunner, notifications)
}
