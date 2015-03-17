package com.seanshubin.detangler.core

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
      val rootNodes = detangled.topLevelUnits()
      generateNodesReport(reportDir, rootNodes)
    }

    def generateNodesReport(path: Path, unitIds: Set[UnitId]): Unit = {
      val nodesReportPath = path.resolve("nodes.txt")
      val arrows = detangled.arrowsFor(unitIds)
      generateArrowsReport(path, arrows)
      val unitInfos = unitIds.toSeq.sorted.map(detangled.map)
      val lines = unitInfos.flatMap(unitInfoToLines)
      val javaLines = JavaConversions.asJavaIterable(lines)
      fileSystem.write(nodesReportPath, javaLines, charset)
      unitIds.foreach(generateNodeReport(path, _))
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

    def generateNodeReport(path: Path, unitId: UnitId): Unit = {
      val nodes = detangled.map(unitId).composedOf
      if (nodes.nonEmpty) {
        val dir = path.resolve(unitId.paths.head.toSeq.sorted.mkString("-"))
        fileSystem.createDirectories(dir)
        generateNodesReport(dir, nodes)
      }
    }
  }

}
