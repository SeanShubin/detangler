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
}
