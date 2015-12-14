package com.seanshubin.detangler.report

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Single}

class Reporter(detangled: Detangled,
               directory: Path,
               filesContract: FilesContract,
               charset: Charset,
               classLoader: ClassLoaderContract,
               pageTemplateRules: PageTemplateRules) extends Runnable {
  override def run(): Unit = {
    filesContract.createDirectories(directory)
    copyResource("style.css", directory.resolve("style.css"))
    generatePage(detangled.root())
  }

  private def copyResource(name: String, destination: Path): Unit = {
    val inputStream = classLoader.getResourceAsStream(name)
    if (inputStream == null) throw new RuntimeException(s"Unable to find resource named '$name'")
    val outputStream = filesContract.newOutputStream(destination)
    IoUtil.copyInputStreamToOutputStream(inputStream, outputStream)
  }

  private def generatePage(module: Module): Unit = {
    val pageTemplateInputStream = classLoader.getResourceAsStream("template.html")
    val pageTemplate = HtmlElement.pageFromInputStream(pageTemplateInputStream, charset)
    module match {
      case single: Single =>
        val children = detangled.children(single)
        if (children.nonEmpty) {
          val content = pageTemplateRules.generate(pageTemplate, single).text
          val fileName = HtmlUtil.fileNameFor(single)
          val file = directory.resolve(fileName)
          filesContract.write(file, content.getBytes(charset))
          children.foreach(generatePage)
        }
      case cycle: Cycle =>
    }
  }
}
