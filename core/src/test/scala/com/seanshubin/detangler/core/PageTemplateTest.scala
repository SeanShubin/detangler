package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.jsoup.Jsoup
import org.scalatest.FunSuite

class PageTemplateTest extends FunSuite {
  val templateText =
    """<div>
      |<ul class="modules">
      |    <li class="module">
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

  test("prototype") {
    println(templateText)
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

  def emitPage(module: Module): Unit = {
    val template = HtmlFragment.fromText(templateText)
    val pageTemplate = new PageTemplate(module, SampleData.detangled, template)
    val fragment = pageTemplate.generate()
    val templatePath = Paths.get("core", "src", "main", "resources", "module.html")
    val generatedPath = Paths.get("generated", "prototype")
    val outPath = generatedPath.resolve(HtmlUtil.fileNameFor(module))
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

/*
  test("top report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.pageFor(SampleData.detangled, SampleData.idRoot)
    assert(page.fileName === "index.html")
    assert(page.modules.size === 2)
    val groupA = page.modules.head
    assert(groupA.id === "group_a")
    assert(groupA.name === "group/a")
    assert(groupA.depth === "1")
    assert(groupA.complexity === "2")
    assert(groupA.partsAnchor.name === "parts")
    assert(groupA.partsAnchor.link === "group_a.html")
    assert(groupA.dependsOnCaption === "depends on (1)")
    assert(groupA.dependsOn.size === 1)
    assert(groupA.dependedOnByCaption === "depended on by (0)")
    assert(groupA.dependedOnBy.size === 0)
    assert(groupA.dependsOnExternalCaption === "depends on external (0)")
    assert(groupA.dependsOnExternal.size === 0)
    assert(groupA.dependedOnByExternalCaption === "depended on by external (0)")
    assert(groupA.dependedOnByExternal.size === 0)
    val groupB = groupA.dependsOn.head
    assert(groupB.anchor.name === "group/b")
    assert(groupB.anchor.link === "#group_b")
    assert(groupB.depth === "3")
    assert(groupB.complexity === "4")
    assert(groupB.reasonAnchor.name === "reason")
    assert(groupB.reasonAnchor.link === "#group_a---group_b")
    assert(page.reasons.size === 1)
    val groupAtoGroupB = page.reasons.head
    assert(groupAtoGroupB.from.name === "group/a")
    assert(groupAtoGroupB.from.link === "#group_a")
    assert(groupAtoGroupB.to.name === "group/b")
    assert(groupAtoGroupB.to.link === "#group_b")
    assert(groupAtoGroupB.reasons.size === 1)
    val packageAtoPackageB = groupAtoGroupB.reasons.head
    assert(packageAtoPackageB.from.name === "package/c")
    assert(packageAtoPackageB.from.link === "group_a.html#group_a--package_c")
    assert(packageAtoPackageB.to.name === "package/e")
    assert(packageAtoPackageB.to.link === "group_b.html#group_b--package_e")
    assert(packageAtoPackageB.reasons.size === 1)
    val classAtoClassD = packageAtoPackageB.reasons.head
    assert(classAtoClassD.from.name === "class/f")
    assert(classAtoClassD.from.link === "group_a--package_c.html#group_a--package_c--class_f")
    assert(classAtoClassD.to.name === "class/i")
    assert(classAtoClassD.to.link === "group_b--package_e.html#group_b--package_e--class_i")
    assert(classAtoClassD.reasons.size === 0)
  }

  test("middle report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.pageFor(SampleData.detangled, SampleData.idGroupA)
    assert(page.fileName === "group_a.html")
    assert(page.modules.size === 2)
    val packageA = page.modules.head
    assert(packageA.id === "group_a--package_c")
    assert(packageA.name === "package/c")
    assert(packageA.depth === "5")
    assert(packageA.complexity === "6")
    assert(packageA.partsAnchor.name === "parts")
    assert(packageA.partsAnchor.link === "group_a--package_c.html")
    assert(packageA.dependsOn.size === 2)
    assert(packageA.dependedOnBy.size === 0)
    assert(packageA.dependsOnExternal.size === 0)
    assert(packageA.dependedOnByExternal.size === 0)
    val packageB = packageA.dependsOn.head
    assert(packageB.anchor.name === "package/d")
    assert(packageB.anchor.link === "#group_a--package_d")
    assert(packageB.depth === "7")
    assert(packageB.complexity === "8")
    assert(packageB.reasonAnchor.name === "reason")
    assert(packageB.reasonAnchor.link === "#group_a--package_c---group_a--package_d")
    assert(page.reasons.size === 1)
    val packageAToPackageB = page.reasons.head
    assert(packageAToPackageB.from.name === "package/c")
    assert(packageAToPackageB.from.link === "#group_a--package_c")
    assert(packageAToPackageB.to.name === "package/d")
    assert(packageAToPackageB.to.link === "#group_a--package_d")
    assert(packageAToPackageB.reasons.size === 1)
    val classAtoClassC = packageAToPackageB.reasons.head
    assert(classAtoClassC.from.name === "class/f")
    assert(classAtoClassC.from.link === "group_a--package_c.html#group_a--package_c--class_f")
    assert(classAtoClassC.to.name === "class/h")
    assert(classAtoClassC.to.link === "group_a--package_d.html#group_a--package_d--class_h")
    assert(classAtoClassC.reasons.size === 0)
  }

  test("bottom report") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val page = reportTransformer.pageFor(SampleData.detangled, SampleData.idPackageC)
    assert(page.fileName === "group_a--package_c.html")
    assert(page.modules.size === 2)
    val classA = page.modules.head
    assert(classA.id === "group_a--package_c--class_f")
    assert(classA.name === "class/f")
    assert(classA.depth === "11")
    assert(classA.complexity === "12")
    assert(classA.dependsOn.size === 3)
    assert(classA.dependedOnBy.size === 0)
    assert(classA.dependsOnExternal.size === 0)
    assert(classA.dependedOnByExternal.size === 0)
    val classB = classA.dependsOn.head
    assert(classB.anchor.name === "class/g")
    assert(classB.anchor.link === "#group_a--package_c--class_g")
    assert(classB.depth === "13")
    assert(classB.complexity === "14")
  }

  test("class level module html strings") {
    val Module = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"), Set("c/e", "c/f"))
    val parent = Module.parent
    assert(HtmlUtil.htmlId(Module) === "g_a--p_b-p_c-p_d--c_e-c_f")
    assert(HtmlUtil.htmlName(Module) === "c/e-c/f")
    assert(HtmlUtil.htmlLink(parent, Module) === "#g_a--p_b-p_c-p_d--c_e-c_f")
    assert(HtmlUtil.htmlLink(UnitId.Root, Module) === "g_a--p_b-p_c-p_d.html#g_a--p_b-p_c-p_d--c_e-c_f")
  }

  test("package level module html strings") {
    val Module = UnitId.complex(Set("g/a"), Set("p/b", "p/c", "p/d"))
    val parent = Module.parent
    assert(HtmlUtil.htmlId(Module) === "g_a--p_b-p_c-p_d")
    assert(HtmlUtil.htmlName(Module) === "p/b-p/c-p/d")
    assert(HtmlUtil.htmlLink(parent, Module) === "#g_a--p_b-p_c-p_d")
    assert(HtmlUtil.htmlLink(UnitId.Root, Module) === "g_a.html#g_a--p_b-p_c-p_d")
    assert(HtmlUtil.fileNameFor(Module) === "g_a--p_b-p_c-p_d.html")
  }

  test("top level module html strings") {
    val Module = SampleData.idGroupA
    assert(HtmlUtil.htmlId(SampleData.idGroupA) === "group_a")
    assert(HtmlUtil.htmlName(SampleData.idGroupA) === "group/a")
    assert(HtmlUtil.htmlLink(UnitId.Root, SampleData.idGroupA) === "#group_a")
    assert(HtmlUtil.htmlLink(SampleData.idGroupB, SampleData.idGroupA) === "index.html#group_a")
    assert(HtmlUtil.fileNameFor(Module) === "group_a.html")
    assert(HtmlUtil.fileNameFor(UnitId.Root) === "index.html")
  }

  test("class level reason html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val from = UnitId.simple("g/a", "p/a", "c/a")
    val to = UnitId.simple("g/b", "p/c", "c/d")
    assert(reportTransformer.reasonId(from, to) === "g_a--p_a--c_a---g_b--p_c--c_d")
    assert(reportTransformer.reasonName(from, to) === "reason")
  }

  test("package level reason html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val from = UnitId.simple("g/a", "p/a")
    val to = UnitId.simple("g/b", "p/c")
    assert(reportTransformer.reasonId(from, to) === "g_a--p_a---g_b--p_c")
    assert(reportTransformer.reasonName(from, to) === "reason")
  }

  test("top level reason html strings") {
    val reportTransformer: ReportTransformer = new ReportTransformerImpl()
    val from = UnitId.simple("g/a")
    val to = UnitId.simple("g/b")
    assert(reportTransformer.reasonId(from, to) === "g_a---g_b")
    assert(reportTransformer.reasonName(from, to) === "reason")
  }
*/
