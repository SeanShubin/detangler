package com.seanshubin.detangler.core

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.devon.core.devon.DevonMarshaller
import org.jsoup.Jsoup

class ReporterImpl(reportDir: Path,
                   files: FilesContract,
                   devonMarshaller: DevonMarshaller,
                   charset: Charset,
                   resourceLoader: ResourceLoader,
                   detangled: Detangled) extends Runnable {

  override def run(): Unit = {
    initDestinationDirectory()
    val inputStream = resourceLoader.inputStreamFor("unit.html")
    val templateText = IoUtil.inputStreamToString(inputStream, charset)
    inputStream.close()
    generateReportForUnit(detangled, UnitId.Root, templateText)
  }

  private def initDestinationDirectory(): Unit = {
    files.createDirectories(reportDir)
    val in = resourceLoader.inputStreamFor("style.css")
    val styleSheetPath = reportDir.resolve("style.css")
    val out = files.newOutputStream(styleSheetPath)
    IoUtil.copyInputStreamToOutputStream(in, out)
  }

  private def generateReportForUnit(detangled: Detangled, unitId: UnitId, templateText: String): Unit = {
    val composedOf = detangled.composedOf(unitId).filterNot(_.isCycle)
    if (composedOf.nonEmpty) {
      val fileName = HtmlUtil.fileNameFor(unitId)
      val pageText = htmlContent(unitId, detangled, templateText)
      val pagePath = reportDir.resolve(fileName)
      val bytes = pageText.getBytes(charset)
      files.write(pagePath, bytes)
      for {
        child <- composedOf
      } {
        generateReportForUnit(detangled, child, templateText)
      }
    }
  }

  private def htmlContent(unit: UnitId, detangled: Detangled, templateText: String): String = {
    val template = HtmlFragment.fromText(templateText)
    val pageTemplate = new PageTemplate(unit, detangled, template)
    val fragment = pageTemplate.generate()
    val baseUri = ""
    val document = Jsoup.parse(templateText, baseUri)
    val elements = fragment.clonedElement.children()
    document.body.children().remove()
    for {
      i <- 0 until elements.size()
    } {
      document.body.appendChild(elements.get(i))
    }
    document.outputSettings().indentAmount(2)
    document.toString
  }
}
