package com.seanshubin.detangler.console

import java.nio.file.Path

import com.seanshubin.detangler.analysis.{CycleFinder, CycleFinderWarshall, Detangler, DetanglerImpl}
import com.seanshubin.detangler.contract.{FilesContract, FilesDelegate}
import com.seanshubin.detangler.core._
import com.seanshubin.detangler.model.{Detangled, Standalone}
import com.seanshubin.detangler.scanner._

trait AfterConfigurationWiring {
  lazy val createReporter: (Detangled, Path) => Runnable = (theDetangled, theReportDir) => new ReporterWiring {
    override def detangled: Detangled = theDetangled

    override def reportDir: Path = theReportDir
  }.reporter
  lazy val filesContract: FilesContract = FilesDelegate
  lazy val directoryScanner: DirectoryScanner = new DirectoryScannerImpl(filesContract, searchPaths)
  lazy val zipScanner: ZipScanner = new ZipScannerImpl
  lazy val classScanner: ClassScanner = new ClassScannerImpl(filesContract)
  lazy val fileScanner: FileScanner = new FileScannerImpl(zipScanner, classScanner)
  lazy val classBytesScanner: ClassBytesScanner = new ClassBytesScannerImpl
  lazy val scanner: Scanner = new ScannerImpl(directoryScanner, fileScanner, classBytesScanner)
  lazy val cycleFinder: CycleFinder[Standalone] = new CycleFinderWarshall[Standalone]
  lazy val detangler: Detangler = new DetanglerImpl(cycleFinder)
  lazy val analyzer: Runnable = new AfterConfigurationRunnerImpl(scanner, detangler, createReporter, reportDir)

  def searchPaths: Seq[Path]

  def reportDir: Path
}
