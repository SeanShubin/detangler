package com.seanshubin.detangler.report

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.detangler.graphviz.GraphGenerator
import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  test("generate report") {
    //given
    val templatePageText = "template text"
    val path = Paths.get("generated", getClass.getSimpleName)
    val charset = StandardCharsets.UTF_8
    val filesStub = new FilesStub(charset)
    val resourceMap = Map(
      "style.css" -> "style text",
      "template.html" -> templatePageText
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
    val graphTemplateRules = new GraphTemplateRulesStub(charset)
    val graphGenerator: GraphGenerator = ???
    val reporter: Runnable = new Reporter(
      SampleData.detangled,
      path,
      filesStub,
      charset,
      classLoader,
      pageTemplateRules,
      graphTemplateRules,
      graphGenerator)

    //when
    reporter.run()

    //then
    val setDifference = SetDifference(filesStub.fileNames(), Set(
      "group-a--package-c.html",
      "group-a--package-d.html",
      "group-a.html",
      "group-b--package-e.html",
      "group-b.html",
      "index.html",
      "style.css"
    ))
    assert(setDifference.isSame, setDifference.message)
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
