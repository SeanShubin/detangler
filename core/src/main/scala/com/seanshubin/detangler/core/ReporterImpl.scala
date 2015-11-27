package com.seanshubin.detangler.core

import java.io.{BufferedWriter, PrintWriter}
import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.devon.core.devon.DevonMarshaller

import scala.collection.JavaConversions

class ReporterImpl(reportDir: Path,
                   files: FilesContract,
                   devonMarshaller: DevonMarshaller,
                   charset: Charset,
                   reportTransformer: ReportTransformer,
                   pageGenerator: PageGenerator,
                   resourceLoader: ResourceLoader,
                   detangled: Detangled) extends Runnable {

  override def run(): Unit = {
    initDestinationDirectory()
    generateReportForUnit(detangled, UnitId.Root)
  }

  private def initDestinationDirectory(): Unit = {
    files.createDirectories(reportDir)
    val in = resourceLoader.inputStreamFor("style.css")
    val styleSheetPath = reportDir.resolve("style.css")
    val out = files.newOutputStream(styleSheetPath)
    IoUtil.copyInputStreamToOutputStream(in, out)
  }

  private def generateReportForUnit(detangled: Detangled, unitId: UnitId): Unit = {
    val composedOf = detangled.composedOf(unitId)
    if (composedOf.nonEmpty) {
      val fileName = HtmlUtil.fileNameFor(unitId)
      val pageText = pageGenerator.pageForId(unitId)
      val pagePath = reportDir.resolve(fileName)
      val bytes = pageText.getBytes(charset)
      files.write(pagePath, bytes)
      for {
        child <- composedOf
      } {
        generateReportForUnit(detangled, child)
      }
    }
  }

  private class Delegate(detangled: Detangled) {
    def generateReports(): Unit = {
      files.createDirectories(reportDir)
      val rootUnits = detangled.composedOf(UnitId.Root)
      generateReport(reportDir, rootUnits)
    }

    def generateReport(path: Path, unitIds: Seq[UnitId]): Unit = {
      val arrows = detangled.arrowsFor(unitIds)
      generateArrowsReport(path, arrows)
      unitIds.foreach(generateDependencyReport(path, _))
      val htmlReportPath = path.resolve("index.html")
      withOutputStream(htmlReportPath) {
        out =>
          new HtmlGenerator(new PrintWriter(out), detangled).generateIndex(unitIds, arrows)
      }
    }

    private def withOutputStream(path: Path)(f: BufferedWriter => Unit) = {
      val out = files.newBufferedWriter(path, charset)
      try {
        f(out)
      } finally {
        out.close()
      }
    }

    def unitInfoToLines(unitInfo: UnitInfo): Seq[String] = {
      devonMarshaller.valueToPretty(unitInfo)
    }

    def generateArrowsReport(path: Path, arrows: Seq[Arrow]): Unit = {
      val arrowsReportPath = path.resolve("arrows.txt")
      val lines = arrows.flatMap(arrowToLines)
      val javaLines = JavaConversions.asJavaIterable(lines)
      files.write(arrowsReportPath, javaLines, charset)
    }

    def arrowToLines(arrow: Arrow): Seq[String] = {
      devonMarshaller.valueToPretty(arrow)
    }

    def generateDependencyReport(path: Path, unitId: UnitId): Unit = {
      val units = detangled.composedOf(unitId)
      if (units.nonEmpty) {
        val dir = path.resolve(unitId.fileSystemName)
        files.createDirectories(dir)
        generateReport(dir, units)
      }
    }
  }

}
