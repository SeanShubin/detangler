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
  lazy val fileScanner: FileScanner = new FileScannerImpl(filesContract, searchPaths)
  lazy val jarScanner: JarScanner = new JarScannerImpl
  lazy val classScanner: ClassScanner = new ClassScannerImpl
  lazy val scanner: Scanner = new ScannerImpl(fileScanner, jarScanner, classScanner)
  lazy val cycleFinder: CycleFinder[Standalone] = new CycleFinderWarshall[Standalone]
  lazy val detangler: Detangler = new DetanglerImpl(cycleFinder)
  lazy val analyzer: Runnable = new AfterConfigurationRunnerImpl(scanner, detangler, createReporter, reportDir)

  def searchPaths: Seq[Path]

  def reportDir: Path
}
