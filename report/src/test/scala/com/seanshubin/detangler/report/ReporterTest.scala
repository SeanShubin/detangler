package com.seanshubin.detangler.report

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.detangler.collection.SetDifference
import com.seanshubin.detangler.contract.ProcessBuilderContract
import com.seanshubin.detangler.graphviz.GraphGenerator
import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  test("generate report") {
    //given
    val pageTemplateText = "page template text"
    val summaryTemplateText = "summary template text"
    val graphTemplateText = "graph template text"
    val reportTemplateText = "report template text"
    val path = Paths.get("generated", getClass.getSimpleName)
    val charset = StandardCharsets.UTF_8
    val filesStub = new FilesStub(charset)
    val graphTarget = Seq("graph target")
    val graphTemplateElement = HtmlElement.fragmentFromString("<p>graph</p>")
    val tableOfContentsTemplateElement = HtmlElement.fragmentFromString("<p>table-of-contents</p>")
    val resourceMap = Map(
      "graph.html" -> graphTemplateText,
      "style.css" -> "style text",
      "template.html" -> pageTemplateText,
      "summary.html" -> summaryTemplateText,
      "report.html" -> reportTemplateText
    )
    val configurationLines = Seq("configuration line")
    def allowCyclesConfigurationLines(cycles: Seq[Seq[String]]): Seq[String] = Seq("allow cycles configuration line")
    val classLoader = new ClassLoaderStub(resourceMap, charset)
    val pageTextMap = Map(
      SampleData.root -> "<p>index text</p>",
      SampleData.groupA -> "<p>a text</p>",
      SampleData.groupB -> "<p>b text</p>",
      SampleData.packageC -> "<p>c text</p>",
      SampleData.packageD -> "<p>d text</p>",
      SampleData.packageE -> "<p>e text</p>"
    )
    val pageTemplateRules = new PageTemplateRulesStub(pageTextMap, charset)
    val graphTemplateRules = new GraphTemplateRulesStub(graphTemplateElement)
    val tableOfContentsRules = new TableOfContentsTemplateRulesStub(tableOfContentsTemplateElement)
    val summaryTemplateRules = new SummaryTemplateRulesStub()
    val graphGenerator: GraphGenerator = new GraphGeneratorStub(graphTarget)
    val process = new ProcessStub
    val createProcessBuilder: Seq[String] => ProcessBuilderContract =
      command => ProcessBuilderStub(command, process)
    val allowedInCycles: Seq[Standalone] = Seq()
    def notifyNewCycleParts(newCycleParts: Seq[Standalone]): Unit = {}

    val reporter: () => ReportResult = new Reporter(
      SampleData.detangled,
      allowedInCycles,
      path,
      filesStub,
      charset,
      classLoader,
      summaryTemplateRules,
      pageTemplateRules,
      graphTemplateRules,
      tableOfContentsRules,
      graphGenerator,
      createProcessBuilder,
      configurationLines,
      allowCyclesConfigurationLines,
      notifyNewCycleParts)

    //when
    reporter.apply()

    //then
    val setDifference = SetDifference.diff(filesStub.fileNames(), Set(
      "report--group-a--package-c.html",
      "report--group-a--package-d.html",
      "report--group-a.html",
      "report--group-b--package-e.html",
      "report--group-b.html",
      "index.html",
      "report.html",
      "graph--group-a--package-c.html",
      "graph--group-a--package-d.html",
      "graph--group-a.html",
      "graph--group-b--package-e.html",
      "graph--group-b.html",
      "graph.html",
      "style.css"
    ))
    assert(setDifference.isSame, setDifference.messageLines.mkString("\n"))
    assert(filesStub.stringContentsOf("style.css") === "style text")
    assert(filesStub.stringContentsOf("report--group-a.html") === "<p>a text</p>")
    assert(filesStub.stringContentsOf("report--group-b.html") === "<p>b text</p>")
    assert(filesStub.stringContentsOf("report--group-a--package-c.html") === "<p>c text</p>")
    assert(filesStub.stringContentsOf("report--group-a--package-d.html") === "<p>d text</p>")
    assert(filesStub.stringContentsOf("report--group-b--package-e.html") === "<p>e text</p>")
    assert(filesStub.directoriesCreated === Seq(Paths.get("generated", "ReporterTest")))
  }
}
