package com.seanshubin.detangler.report

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.detangler.contract.{ClassLoaderContract, FilesContract}
import com.seanshubin.detangler.model.{Detangled, Standalone}

class Reporter(detangled: Detangled,
               directory: Path,
               filesContract: FilesContract,
               charset: Charset,
               classLoader: ClassLoaderContract,
               pageTemplateRules: PageTemplateRules) extends Runnable {
  override def run(): Unit = {
    filesContract.createDirectories(directory)
    copyResource("style.css", directory.resolve("style.css"))
    generatePage(Standalone.Root)
  }

  private def copyResource(name: String, destination: Path): Unit = {
    val inputStream = classLoader.getResourceAsStream(name)
    if (inputStream == null) throw new RuntimeException(s"Unable to find resource named '$name'")
    val outputStream = filesContract.newOutputStream(destination)
    IoUtil.copyInputStreamToOutputStream(inputStream, outputStream)
  }

  private def generatePage(standalone: Standalone): Unit = {
    val pageTemplateInputStream = classLoader.getResourceAsStream("template.html")
    val pageTemplate = HtmlElement.pageFromInputStream(pageTemplateInputStream, charset)
    val children = detangled.childStandalone(standalone)
    if (children.nonEmpty) {
      val isLeafPage = standalone.path.size >= detangled.levelsDeep - 1
      val content = pageTemplateRules.generate(pageTemplate, standalone, isLeafPage).toString
      val fileName = HtmlRendering.fileNameFor(standalone)
      val file = directory.resolve(fileName)
      filesContract.write(file, content.getBytes(charset))
      children.foreach(generatePage)
    }
  }
}
