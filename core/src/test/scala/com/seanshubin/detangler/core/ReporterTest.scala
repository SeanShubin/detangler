package com.seanshubin.detangler.core

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Path, Paths}

import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ReporterTest extends FunSuite with EasyMockSugar {
  test("generate html pages") {
    val reportDir: Path = Paths.get("sample", "path")
    val files: FilesContract = mock[FilesContract]
    val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
    val charset: Charset = StandardCharsets.UTF_8
    val reportTransformer: ReportTransformer = null
    val pageGenerator: PageGenerator = mock[PageGenerator]
    val resourceLoader: ResourceLoader = mock[ResourceLoader]
    val detangled: Detangled = SampleData.detangled
    val styleCss = new FakeFile("style", charset)
    val outputStylePath = reportDir.resolve("style.css")
    val reporter = new ReporterImpl(
      reportDir,
      files,
      devonMarshaller,
      charset,
      reportTransformer,
      pageGenerator,
      resourceLoader,
      detangled)

    def expectPageWrite(id: UnitId, name: String): Unit = {
      val text = "content"
      pageGenerator.pageForId(id).andReturn(text)
      files.write(reportDir.resolve(name), text.getBytes(charset)).andReturn(null)
    }

    expecting {
      files.createDirectories(reportDir).andReturn(reportDir)
      resourceLoader.inputStreamFor("style.css").andReturn(styleCss.createInputStream())
      files.newOutputStream(outputStylePath).andReturn(styleCss.createOutputStream())
      expectPageWrite(SampleData.idRoot, "index.html")
      expectPageWrite(SampleData.idGroupA, "group_a.html")
      expectPageWrite(SampleData.idPackageA, "group_a--package_c.html")
      expectPageWrite(SampleData.idPackageB, "group_a--package_d.html")
      expectPageWrite(SampleData.idGroupB, "group_b.html")
      expectPageWrite(SampleData.idPackageC, "group_b--package_e.html")
    }
    whenExecuting(files, pageGenerator, resourceLoader) {
      reporter.run()
    }
  }
}
