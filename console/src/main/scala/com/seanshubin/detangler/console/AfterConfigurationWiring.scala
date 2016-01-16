package com.seanshubin.detangler.console

import java.nio.file.Path
import java.time.Clock

import com.seanshubin.detangler.analysis._
import com.seanshubin.detangler.bytecode.{ClassParser, ClassParserImpl}
import com.seanshubin.detangler.contract.{FilesContract, FilesDelegate}
import com.seanshubin.detangler.core._
import com.seanshubin.detangler.model.{Detangled, Standalone}
import com.seanshubin.detangler.scanner._
import com.seanshubin.detangler.timer.{Timer, TimerImpl}
import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}

trait AfterConfigurationWiring {
  lazy val emitLine: String => Unit = println
  lazy val clock: Clock = Clock.systemUTC()
  lazy val createReporter: (Detangled, Path, Seq[Standalone]) => Runnable = (theDetangled, theReportDir, theAllowedCycles) => new ReporterWiring {
    override def detangled: Detangled = theDetangled

    override def reportDir: Path = theReportDir

    override def allowedCycles: Seq[Standalone] = theAllowedCycles

    override def configurationWriter: ConfigurationWriter = AfterConfigurationWiring.this.configurationWriter
  }.reporter
  lazy val filesContract: FilesContract = FilesDelegate
  lazy val directoryScanner: DirectoryScanner =
    new DirectoryScannerImpl(filesContract, searchPaths, ignoreFiles)
  lazy val stringToStandaloneFunction: String => Option[Standalone] = new StringToStandaloneFunction(
    level,
    startsWithInclude,
    startsWithExclude,
    startsWithDrop)
  lazy val acceptNameFunction: String => Boolean = new AcceptNameFunction(stringToStandaloneFunction)
  lazy val zipScanner: ZipScanner = new ZipScannerImpl(
    filesContract,
    FileTypes.isCompressed,
    acceptNameFunction,
    notifications.warnNoRelevantClassesInPath)
  lazy val classScanner: ClassScanner = new ClassScannerImpl(filesContract)
  lazy val timer: Timer = new TimerImpl(clock, notifications.startTiming, notifications.endTiming)
  lazy val fileScanner: FileScanner = new FileScannerImpl(zipScanner, classScanner, timer)
  lazy val classParser: ClassParser = new ClassParserImpl
  lazy val classBytesScanner: ClassBytesScanner = new ClassBytesScannerImpl(classParser)
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val notifications: Notifications = new LineEmittingNotifications(devonMarshaller, emitLine)
  lazy val scanner: Scanner = new ScannerImpl(
    directoryScanner,
    fileScanner,
    classBytesScanner,
    timer)
  lazy val cycleFinder: CycleFinder[Standalone] = new CycleFinderWarshall[Standalone]
  lazy val detangler: Detangler = new DetanglerImpl(cycleFinder)
  lazy val analyzer: Runnable = new AfterConfigurationRunnerImpl(
    scanner, detangler, createReporter, reportDir, allowedCycles, stringToStandaloneFunction, timer)

  def searchPaths: Seq[Path]

  def reportDir: Path

  def level: Int

  def startsWithInclude: Seq[Seq[String]]

  def startsWithExclude: Seq[Seq[String]]

  def startsWithDrop: Seq[Seq[String]]

  def allowedCycles: Seq[Seq[String]]

  def configurationWriter: ConfigurationWriter

  def ignoreFiles: Seq[Path]
}
