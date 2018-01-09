package com.seanshubin.detangler.report

import java.io.{IOException, InputStream}
import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.detangler.contract.{ClassLoaderContract, FilesContract, ProcessBuilderContract}
import com.seanshubin.detangler.graphviz.GraphGenerator
import com.seanshubin.detangler.model.{Detangled, Standalone}

import scala.collection.JavaConverters._

class Reporter(detangled: Detangled,
               allowedCycles: Seq[Standalone],
               directory: Path,
               filesContract: FilesContract,
               charset: Charset,
               classLoader: ClassLoaderContract,
               summaryTemplateRules: SummaryTemplateRules,
               pageTemplateRules: PageTemplateRules,
               graphTemplateRules: GraphTemplateRules,
               graphGenerator: GraphGenerator,
               createProcessBuilder: Seq[String] => ProcessBuilderContract,
               configurationLines: Seq[String],
               allowCyclesConfigurationLines: Seq[Seq[String]] => Seq[String],
               notifyNewCycleParts: Seq[Standalone] => Unit) extends (() => ReportResult) {

  val summaryTemplate = loadTemplate("summary.html")
  val pageTemplate = loadTemplate("report.html")
  val graphTemplate = loadTemplate("graph.html")

  override def apply(): ReportResult = {
    filesContract.createDirectories(directory)
    copyResource("style.css", directory.resolve("style.css"))
    generateSummary()
    generateConfiguration(configurationLines)
    generateAllowCyclesConfiguration()
    generatePages(Standalone.Root)
    checkForNewCycles()
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

  private def generateSummary(): Unit = {
    val content = summaryTemplateRules.generate(summaryTemplate).toString
    val fileName = HtmlRender.navigateHigherLink(Standalone.Root)
    val file = directory.resolve(fileName)
    filesContract.write(file, content.getBytes(charset))
  }

  private def generatePages(standalone: Standalone): Unit = {
    val children = detangled.childStandalone(standalone)
    if (children.nonEmpty) {
      val isLeafPage = standalone.path.size >= detangled.levelsDeep - 1
      generateDependenciesPage(standalone, pageTemplate, isLeafPage)
      generateGraphSource(standalone)
      generateGraphPage(standalone, graphTemplate)
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
    val fileName = HtmlRender.reportPageLink(standalone)
    val file = directory.resolve(fileName)
    filesContract.write(file, content.getBytes(charset))
  }

  private def generateGraphPage(standalone: Standalone, graphTemplate: HtmlElement): Path = {
    val renderGraphResult = renderGraph(standalone)
    val content = graphTemplateRules.generate(graphTemplate, standalone, renderGraphResult).toString
    val fileName = HtmlRender.graphLink(standalone)
    val file = directory.resolve(fileName)
    filesContract.write(file, content.getBytes(charset))
  }

  private def generateGraphSource(standalone: Standalone): Path = {
    val lines = graphGenerator.generate(
      detangled.plainDependsOnFor(standalone),
      detangled.plainCyclesFor(standalone),
      detangled.plainEntryPointsFor(standalone))
    val fileName = HtmlRender.graphSourceLink(standalone)
    val file = directory.resolve(fileName)
    val javaLines = lines.asJava
    filesContract.write(file, javaLines, charset)
  }

  private def renderGraph(standalone: Standalone): GraphRenderResult = {
    val source = HtmlRender.graphSourceLink(standalone)
    val target = HtmlRender.graphTargetLink(standalone)
    val command = Seq("dot", "-Tsvg", s"-o$target", source)

    val processBuilder = createProcessBuilder(command).directory(directory.toFile)
    try {
      processBuilder.start()
      GraphRenderSuccess
    } catch {
      case ex: IOException =>
        GraphRenderFailure(command)
    }
  }

  private def generateConfiguration(lines: Seq[String]): Unit = {
    writeLines("effective-configuration.txt", lines)
  }

  private def generateAllowCyclesConfiguration(): Unit = {
    val plainCycleParts = detangled.cycles().flatMap(_.parts).map(_.path)
    val lines = allowCyclesConfigurationLines(plainCycleParts)
    writeLines("detangler-allowed-in-cycle.txt", lines)
  }

  private def writeLines(fileName: String, lines: Seq[String]): Unit = {
    val javaLines = lines.asJava
    val path = directory.resolve(fileName)
    filesContract.write(path, javaLines, charset)
  }

  private def checkForNewCycles(): ReportResult = {
    val cycles = detangled.cycles()
    val cycleParts = cycles.flatMap(_.parts)
    val newCycleParts = cycleParts.filterNot(allowedCycles.contains)
    notifyNewCycleParts(newCycleParts)
    val reportIndex = directory.resolve("index.html")
    if (newCycleParts.size == 1) {
      ReportResult.Failure(reportIndex, "1 new cycle part")
    } else if (newCycleParts.nonEmpty) {
      ReportResult.Failure(reportIndex, s"${newCycleParts.size} new cycle parts")
    } else {
      ReportResult.Success(reportIndex)
    }
  }
}
