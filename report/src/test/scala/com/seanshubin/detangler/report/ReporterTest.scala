package com.seanshubin.detangler.report

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.detangler.collection.SetDifference
import com.seanshubin.detangler.contract.ProcessBuilderContract
import com.seanshubin.detangler.graphviz.GraphGenerator
import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  test("generate report") {
    //given
    val pageTemplateText = "page template text"
    val graphTemplateText = "graph template text"
    val path = Paths.get("generated", getClass.getSimpleName)
    val charset = StandardCharsets.UTF_8
    val filesStub = new FilesStub(charset)
    val graphTarget = Seq("graph target")
    val graphTemplateElement = HtmlElement.fragmentFromString("<p>graph</p>")
    val resourceMap = Map(
      "template-graph.html" -> graphTemplateText,
      "style.css" -> "style text",
      "template.html" -> pageTemplateText
    )
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
    val summaryTemplateRules = new SummaryTemplateRulesStub()
    val graphGenerator: GraphGenerator = new GraphGeneratorStub(graphTarget)
    val process = new ProcessStub
    val createProcessBuilder: Seq[String] => ProcessBuilderContract =
      (command) => new ProcessBuilderStub(command, process)

    val reporter: Runnable = new Reporter(
      SampleData.detangled,
      path,
      filesStub,
      charset,
      classLoader,
      summaryTemplateRules,
      pageTemplateRules,
      graphTemplateRules,
      graphGenerator,
      createProcessBuilder
    )

    //when
    reporter.run()

    //then
    val setDifference = SetDifference.diff(filesStub.fileNames(), Set(
      "group-a--package-c.html",
      "group-a--package-d.html",
      "group-a.html",
      "group-b--package-e.html",
      "group-b.html",
      "index.html",
      "graph-group-a--package-c.html",
      "graph-group-a--package-d.html",
      "graph-group-a.html",
      "graph-group-b--package-e.html",
      "graph-group-b.html",
      "graph-index.html",
      "style.css"
    ))
    assert(setDifference.isSame, setDifference.messageLines.mkString("\n"))
    assert(filesStub.stringContentsOf("index.html") === "<p>index text</p>")
    assert(filesStub.stringContentsOf("style.css") === "style text")
    assert(filesStub.stringContentsOf("group-a.html") === "<p>a text</p>")
    assert(filesStub.stringContentsOf("group-b.html") === "<p>b text</p>")
    assert(filesStub.stringContentsOf("group-a--package-c.html") === "<p>c text</p>")
    assert(filesStub.stringContentsOf("group-a--package-d.html") === "<p>d text</p>")
    assert(filesStub.stringContentsOf("group-b--package-e.html") === "<p>e text</p>")
    assert(filesStub.directoriesCreated === Seq(Paths.get("generated", "ReporterTest")))
  }
}
