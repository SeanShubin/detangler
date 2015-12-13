package com.seanshubin.detangler.report

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

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
      SampleData.moduleRoot -> "index text",
      SampleData.moduleA -> "a text",
      SampleData.moduleB -> "b text",
      SampleData.moduleC -> "c text",
      SampleData.moduleD -> "d text",
      SampleData.moduleE -> "e text"
    )
    val pageTemplateRules = new PageTemplateRulesStub(pageTextMap, charset)
    val reporter: Runnable = new Reporter(
      SampleData.detangled,
      path,
      filesStub,
      charset,
      classLoader,
      pageTemplateRules)

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
    assert(filesStub.stringContentsOf("index.html") === "index text")
    assert(filesStub.stringContentsOf("style.css") === "style text")
    assert(filesStub.stringContentsOf("group-a.html") === "a text")
    assert(filesStub.stringContentsOf("group-b.html") === "b text")
    assert(filesStub.stringContentsOf("group-a--package-c.html") === "c text")
    assert(filesStub.stringContentsOf("group-a--package-d.html") === "d text")
    assert(filesStub.stringContentsOf("group-b--package-e.html") === "e text")
  }
}
