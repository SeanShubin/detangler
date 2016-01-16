package com.seanshubin.detangler.console

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path

import com.seanshubin.detangler.contract.{FilesContract, FilesDelegate}
import com.seanshubin.detangler.core._
import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}

trait TopLevelWiring {
  lazy val emitLine: String => Unit = println
  lazy val files: FilesContract = FilesDelegate
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val charset: Charset = StandardCharsets.UTF_8
  lazy val notifications: Notifications = new LineEmittingNotifications(devonMarshaller, emitLine)
  lazy val configurationFactory: ConfigurationFactory = new ConfigurationFactoryImpl(
    files, devonMarshaller, charset)
  lazy val createRunner: Configuration => Runnable = (configuration) =>
    new AfterConfigurationWiring {
      override def reportDir: Path = configuration.reportDir

      override def searchPaths: Seq[Path] = configuration.searchPaths

      override def level: Int = configuration.level.get

      override def startsWithDrop: Seq[Seq[String]] = configuration.startsWith.drop

      override def startsWithInclude: Seq[Seq[String]] = configuration.startsWith.include

      override def startsWithExclude: Seq[Seq[String]] = configuration.startsWith.exclude

      override def allowedCycles: Seq[Seq[String]] = configuration.allowedInCycle

      override def ignoreFiles: Seq[Path] = configuration.ignoreFiles

      override def configurationWriter: ConfigurationWriter = new ConfigurationWriterDevon(configuration, devonMarshaller)
    }.analyzer
  lazy val launcher: Runnable = new TopLevelRunnerImpl(
    commandLineArguments, configurationFactory, createRunner, notifications)

  def commandLineArguments: Seq[String]
}
