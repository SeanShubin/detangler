package com.seanshubin.detangler.core

import java.io.BufferedWriter
import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.devon.core.devon.DevonMarshaller
import com.seanshubin.utility.filesystem.FileSystemIntegration

import scala.collection.JavaConversions

class ReporterImpl(reportDir: Path,
                   fileSystem: FileSystemIntegration,
                   devonMarshaller: DevonMarshaller,
                   charset: Charset) extends Reporter {

  override def generateReports(detangled: Detangled): Unit = {
    new Delegate(detangled).generateReports()
  }

  private class Delegate(detangled: Detangled) {
    def generateReports(): Unit = {
      fileSystem.createDirectories(reportDir)
      val rootUnits = detangled.topLevelUnits()
      generateReport(reportDir, rootUnits)
    }

    def generateReport(path: Path, unitIds: Set[UnitId]): Unit = {
      val unitsReportPath = path.resolve("units.txt")
      val arrows = detangled.arrowsFor(unitIds)
      generateArrowsReport(path, arrows)
      val unitInfos = unitIds.toSeq.sorted.map(detangled.map)
      val lines = unitInfos.flatMap(unitInfoToLines)
      val javaLines = JavaConversions.asJavaIterable(lines)
      fileSystem.write(unitsReportPath, javaLines, charset)
      unitIds.foreach(generateDependencyReport(path, _))
      val htmlReportPath = path.resolve("index.html")
      withOutputStream(htmlReportPath) {
        out =>
          new HtmlGenerator(out, detangled).generateIndex(unitInfos, arrows)
      }
    }

    private def withOutputStream(path: Path)(f: BufferedWriter => Unit) = {
      val out = fileSystem.newBufferedWriter(path, charset)
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
      fileSystem.write(arrowsReportPath, javaLines, charset)
    }

    def arrowToLines(arrow: Arrow): Seq[String] = {
      devonMarshaller.valueToPretty(arrow)
    }

    def generateDependencyReport(path: Path, unitId: UnitId): Unit = {
      val units = detangled.map(unitId).composedOf
      if (units.nonEmpty) {
        val dir = path.resolve(unitId.paths.head.toSeq.sorted.mkString("-"))
        fileSystem.createDirectories(dir)
        generateReport(dir, units)
      }
    }
  }

}
