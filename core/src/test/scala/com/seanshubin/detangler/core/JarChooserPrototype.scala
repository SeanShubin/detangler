package com.seanshubin.detangler.core

import java.nio.file.{Path, Paths}

import com.seanshubin.detangler.analysis.{AcceptNameFunction, StringToStandaloneFunction}
import com.seanshubin.detangler.bytecode.{ClassParser, ClassParserImpl}
import com.seanshubin.detangler.contract.{FilesContract, FilesDelegate}
import com.seanshubin.detangler.model.{SourceAndDependencies, Standalone}
import com.seanshubin.detangler.scanner._
import com.seanshubin.detangler.timer.Timer

object JarChooserPrototype extends App {
  val basePath = Paths.get("console/target/detangler.jar")
  val startsWithInclude = Seq(Seq("com", "seanshubin"))
  val startsWithDrop =  Seq(Seq("com", "seanshubin"))

//  val basePath = Paths.get("local-config/tardis")
//  val startsWithInclude = Seq(Seq("com", "cj"), Seq("cj"))
//  val startsWithDrop = Seq(Seq("com", "cj"), Seq("cj"))

  val searchPaths = Seq(basePath)
  val ignoreFiles: Seq[Path] = Seq()
  val level = 3
  val startsWithExclude = Seq()
  val stringToStandaloneFunction: String => Option[Standalone] = new StringToStandaloneFunction(
    level,
    startsWithInclude,
    startsWithExclude,
    startsWithDrop)
  val acceptNameFunction: String => Boolean = new AcceptNameFunction(stringToStandaloneFunction)
  def warnNoRelevantClassesInPath(path: Path): Unit = {}
  val filesContract: FilesContract = FilesDelegate
  val zipScanner: ZipScanner = new ZipScannerImpl(
    filesContract,
    FileTypes.isCompressed,
    acceptNameFunction,
    warnNoRelevantClassesInPath)
  val classScanner: ClassScanner = new ClassScannerImpl(filesContract)
  val classParser: ClassParser = new ClassParserImpl
  val timer: Timer = new Timer {
    override def measureTime[T](name: String)(f: => T): T = {
      println(name)
      f
    }
  }
  val directoryScanner: DirectoryScanner =
    new DirectoryScannerImpl(filesContract, searchPaths, ignoreFiles)
  val fileScanner: FileScanner = new FileScannerImpl(zipScanner, classScanner, timer)
  val classBytesScanner: ClassBytesScanner = new ClassBytesScannerImpl(classParser)
  val scanner: Scanner = new ScannerImpl(
    directoryScanner,
    fileScanner,
    classBytesScanner,
    timer)
  val scanResults = scanner.scanDependencies()

  def convertToStandaloneModule(stringDependency: ScannedDependencies): Option[SourceAndDependencies] = {
    val ScannedDependencies(sourceName, stringKey, stringValues) = stringDependency
    val maybeStandaloneKey = stringToStandaloneFunction(stringKey)
    val standaloneValueOptions = stringValues.map(stringToStandaloneFunction)
    val standaloneValues = standaloneValueOptions.flatten
    def createFromKey(standalone:Standalone):SourceAndDependencies = {
      val dependencies = Map(standalone -> standaloneValues.toSet)
      SourceAndDependencies(sourceName, dependencies)
    }
    maybeStandaloneKey.map(createFromKey)
  }

  val standaloneDependencies:Iterable[SourceAndDependencies] = scanResults.flatMap(convertToStandaloneModule)

  standaloneDependencies.foreach(println)
  println(scanResults.size)
}
