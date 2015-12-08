package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.jsoup.Jsoup
import org.scalatest.FunSuite

class PageTemplateTest extends FunSuite {
  val templateText =
    """<div>
      |<ul class="units">
      |    <li class="unit">
      |        <table class="summary">
      |            <thead>
      |            <tr>
      |                <th>name</th>
      |                <th>depth</th>
      |                <th>complexity</th>
      |                <th>composed of</th>
      |            </tr>
      |            </thead>
      |            <tbody>
      |            <tr>
      |                <td class="name">sample/group</td>
      |                <td class="depth">depth number</td>
      |                <td class="complexity">complexity number</td>
      |                <td><a class="composed-of" href="sample_group_parts">parts</a></td>
      |            </tr>
      |            </tbody>
      |        </table>
      |        <ul class="dependency">
      |            <li>
      |                <table>
      |                    <thead>
      |                    <tr>
      |                        <th class="caption" colspan="4">depends on (number)</th>
      |                    </tr>
      |                    <tr>
      |                        <th>name</th>
      |                        <th>depth</th>
      |                        <th>complexity</th>
      |                        <th>reason</th>
      |                    </tr>
      |                    </thead>
      |                    <tbody class="dependency-row-outer">
      |                    <tr class="dependency-row-inner">
      |                        <td><a class="name" href="#other_group">other/group</a></td>
      |                        <td class="depth">depth</td>
      |                        <td class="complexity">complexity</td>
      |                        <td><a class="reason" href="#other_group_parts">reason</a></td>
      |                    </tr>
      |                    </tbody>
      |                </table>
      |            </li>
      |        </ul>
      |    </li>
      |</ul>
      |<ul class="reasons">
      |    <li class="reason">
      |        <a class="from" href="replace-me">replace-me</a>
      |        <span>&rarr;</span>
      |        <a class="to" href="replace-me">replace-me</a>
      |    </li>
      |</ul>
      |</div>""".stripMargin

  test("root page") {
    val template = HtmlFragment.fromText(templateText)
    val pageTemplate = new PageTemplate(SampleData.idRoot, SampleData.detangled, template)
    val actual = pageTemplate.generate()

    assert(actual.text("#group_a .name") === "group/a")
    assert(actual.text("#group_a .depth") === "1")
    assert(actual.text("#group_a .complexity") === "2")
    assert(actual.attr("#group_a .composed-of", "href") === "group_a.html")
    assert(actual.text("#group_a .composed-of") === "parts")
//    assert(actual.text("#group_a .dependency .caption") === "depends on (1)")
//    assert(actual.text("#group_a .dependency .name") === "group/b")
//    assert(actual.attr("#group_a .dependency .name", "href") === "#group_b")
//    assert(actual.text("#group_a .dependency .depth") === "3")
//    assert(actual.text("#group_a .dependency .complexity") === "4")
//    assert(actual.text("#group_a .dependency .reason") === "reason")
//    assert(actual.attr("#group_a .dependency .reason", "href") === "#group_a---group_b")
//
//    assert(actual.text("#group_b .name") === "group/b")
//    assert(actual.text("#group_b .depth") === "3")
//    assert(actual.text("#group_b .complexity") === "4")
//    assert(actual.attr("#group_b .composed-of", "href") === "group_b.html")
//    assert(actual.text("#group_b .composed-of") === "parts")
//    assert(actual.text("#group_b .dependency .caption") === "depended on by (1)")
//    assert(actual.text("#group_b .dependency .name") === "group/a")
//    assert(actual.attr("#group_b .dependency .name", "href") === "#group_a")
//    assert(actual.text("#group_b .dependency .depth") === "1")
//    assert(actual.text("#group_b .dependency .complexity") === "2")
//    assert(actual.text("#group_b .dependency .reason") === "reason")
//    assert(actual.attr("#group_b .dependency .reason", "href") === "#group_a---group_b")
//
//    assert(actual.text("#group_a---group_b .from") === "group/a")
//    assert(actual.attr("#group_a---group_b .from", "href") === "#group_a")
//    assert(actual.text("#group_a---group_b .to") === "group/b")
//    assert(actual.attr("#group_a---group_b .to", "href") === "#group_b")
//    assert(actual.text("#group_a--package_c---group_b--package_e .from") === "package/c")
//    assert(actual.attr("#group_a--package_c---group_b--package_e .from", "href") === "group_a.html#group_a--package_c")
//    assert(actual.text("#group_a--package_c---group_b--package_e .to") === "package/e")
//    assert(actual.attr("#group_a--package_c---group_b--package_e .to", "href") === "group_b.html#group_b--package_e")
//    assert(actual.text("#group_a--package_c--class_f---group_b--package_e--class_i .from") === "class/f")
//    assert(actual.attr("#group_a--package_c--class_f---group_b--package_e--class_i .from", "href") === "group_a--package_c.html#group_a--package_c--class_f")
//    assert(actual.text("#group_a--package_c--class_f---group_b--package_e--class_i .to") === "class/i")
//    assert(actual.attr("#group_a--package_c--class_f---group_b--package_e--class_i .to", "href") === "group_b--package_e.html#group_b--package_e--class_i")
  }

  test("prototype"){
    Files.createDirectories(Paths.get("generated", "prototype"))
    copyStylesheet()
    emitPage(SampleData.idRoot)
    emitPage(SampleData.idGroupA)
    emitPage(SampleData.idGroupB)
    emitPage(SampleData.idPackageC)
    emitPage(SampleData.idPackageD)
    emitPage(SampleData.idPackageE)
  }

  def copyStylesheet(): Unit = {
    val styleSheetSourcePath = Paths.get("core", "src", "main", "resources", "style.css")
    val bytes = Files.readAllBytes(styleSheetSourcePath)
    val styleSheetDestinationPath = Paths.get("generated", "prototype", "style.css")
    Files.write(styleSheetDestinationPath, bytes)
  }

  def emitPage(unit:UnitId): Unit = {
    val template = HtmlFragment.fromText(templateText)
    val pageTemplate = new PageTemplate(unit, SampleData.detangled, template)
    val fragment = pageTemplate.generate()
    val templatePath = Paths.get("core", "src", "main", "resources", "unit.html")
    val generatedPath = Paths.get("generated", "prototype")
    val outPath = generatedPath.resolve(HtmlUtil.fileNameFor(unit))
    val charset = StandardCharsets.UTF_8
    val baseUri = ""
    val inputStream = Files.newInputStream(templatePath)
    val document = Jsoup.parse(inputStream, charset.name(), baseUri)
    inputStream.close()
    val elements = fragment.clonedElement.children()
    document.body.children().remove()
    for {
      i <- 0 until elements.size()
    } {
      document.body.appendChild(elements.get(i))
    }
    document.outputSettings().indentAmount(2)
    Files.write(outPath, document.toString.getBytes(charset))
  }
}
