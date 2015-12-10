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
    val inputStream = resourceLoader.inputStreamFor("template.html")
    val templateText = IoUtil.inputStreamToString(inputStream, charset)
    inputStream.close()
    generateReportForModule(detangled, Module.Root, templateText)
  }

  private def initDestinationDirectory(): Unit = {
    files.createDirectories(reportDir)
    val in = resourceLoader.inputStreamFor("style.css")
    val styleSheetPath = reportDir.resolve("style.css")
    val out = files.newOutputStream(styleSheetPath)
    IoUtil.copyInputStreamToOutputStream(in, out)
  }

  private def generateReportForModule(detangled: Detangled, module: Module, templateText: String): Unit = {
    val composedOf = detangled.composedOf(module).filterNot(_.isCycle)
    if (composedOf.nonEmpty) {
      val fileName = HtmlUtil.fileNameFor(module)
      val pageText = htmlContent(module, detangled, templateText)
      val pagePath = reportDir.resolve(fileName)
      val bytes = pageText.getBytes(charset)
      files.write(pagePath, bytes)
      for {
        child <- composedOf
      } {
        generateReportForModule(detangled, child, templateText)
      }
    }
  }

  private def htmlContent(module: Module, detangled: Detangled, templateText: String): String = {
    val template = HtmlFragment.fromText(templateText)
    val pageTemplate = new PageTemplate(module, detangled, template)
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
