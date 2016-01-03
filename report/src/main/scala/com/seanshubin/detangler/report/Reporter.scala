package com.seanshubin.detangler.report

import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.detangler.contract.{ClassLoaderContract, FilesContract, ProcessBuilderContract}
import com.seanshubin.detangler.graphviz.GraphGenerator
import com.seanshubin.detangler.model.{Detangled, Standalone}

import scala.collection.JavaConversions

class Reporter(detangled: Detangled,
               directory: Path,
               filesContract: FilesContract,
               charset: Charset,
               classLoader: ClassLoaderContract,
               pageTemplateRules: PageTemplateRules,
               graphTemplateRules: GraphTemplateRules,
               graphGenerator: GraphGenerator,
               createProcessBuilder: Seq[String] => ProcessBuilderContract) extends Runnable {
  override def run(): Unit = {
    filesContract.createDirectories(directory)
    copyResource("style.css", directory.resolve("style.css"))
    generatePages(Standalone.Root)
  }

  def loadResource(name: String): InputStream = {
    val inputStream = classLoader.getResourceAsStream(name)
    if (inputStream == null) {
      throw new RuntimeException(s"Unable to load resource '$name'")
    }
    inputStream
  }

  private def copyResource(name: String, destination: Path): Unit = {
    val inputStream = loadResource(name)
    if (inputStream == null) throw new RuntimeException(s"Unable to find resource named '$name'")
    val outputStream = filesContract.newOutputStream(destination)
    IoUtil.copy(inputStream, outputStream)
  }

  private def generatePages(standalone: Standalone): Unit = {
    val pageTemplate = loadTemplate("template.html")
    val graphTemplate = loadTemplate("template-graph.html")
    val children = detangled.childStandalone(standalone)
    if (children.nonEmpty) {
      val isLeafPage = standalone.path.size >= detangled.levelsDeep - 1
      generateDependenciesPage(standalone, pageTemplate, isLeafPage)
      generateGraphPage(standalone, graphTemplate)
      generateGraphSource(standalone)
      renderGraph(standalone)
      children.foreach(generatePages)
    }
  }

  private def loadTemplate(name: String): HtmlElement = {
    val pageTemplateInputStream = loadResource(name)
    val pageTemplate = HtmlElement.pageFromInputStream(pageTemplateInputStream, charset)
    pageTemplate
  }

  private def generateDependenciesPage(standalone: Standalone, pageTemplate: HtmlElement, isLeafPage: Boolean): Path = {
    val content = pageTemplateRules.generate(pageTemplate, standalone, isLeafPage).toString
    val fileName = HtmlRendering.fileNameFor(standalone)
    val file = directory.resolve(fileName)
    filesContract.write(file, content.getBytes(charset))
  }

  private def generateGraphPage(standalone: Standalone, graphTemplate: HtmlElement): Path = {
    val content = graphTemplateRules.generate(graphTemplate, standalone).toString
    val fileName = HtmlRendering.graphLink(standalone)
    val file = directory.resolve(fileName)
    filesContract.write(file, content.getBytes(charset))
  }

  private def generateGraphSource(standalone: Standalone): Path = {
    val lines = graphGenerator.generate(detangled.plainDependsOnFor(standalone), detangled.plainCyclesFor(standalone))
    val fileName = HtmlRendering.graphSourceFile(standalone)
    val file = directory.resolve(fileName)
    val javaLines = JavaConversions.asJavaCollection(lines)
    filesContract.write(file, javaLines, charset)
  }

  private def renderGraph(standalone: Standalone): Unit = {
    val source = HtmlRendering.graphSourceFile(standalone)
    val target = HtmlRendering.graphTargetFile(standalone)
    val command = Seq("dot", "-Tsvg", s"-o$target", source)

    val processBuilder = createProcessBuilder(command).directory(directory.toFile)
    processBuilder.start()
  }
}
