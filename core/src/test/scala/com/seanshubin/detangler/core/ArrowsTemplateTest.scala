package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class ArrowsTemplateTest extends FunSuite {
  test("arrows template") {
    val templateText =
      """<ul class="reasons">
        |    <li class="reason">
        |        <a class="from" href="replace-me">replace-me</a>
        |        <span>&rarr;</span>
        |        <a class="to" href="replace-me">replace-me</a>
        |    </li>
        |</ul>
        | """.stripMargin
    val context = SampleData.idRoot
    val arrows =
      Seq(Arrow(SampleData.idGroupA, SampleData.idGroupB,
        Seq(Arrow(SampleData.idPackageC, SampleData.idPackageE,
          Seq(Arrow(SampleData.idClassF, SampleData.idClassI, Seq()))))))
    val arrowsTemplate = new ArrowsTemplate(templateText, context)
    val actual = arrowsTemplate.generate(arrows)

    val groupElement = actual.one("#group_a---group_b")
    assert(groupElement.firstAttr(".from", "href") === "#group_a")
    assert(groupElement.firstText(".from") === "group/a")
    assert(groupElement.firstAttr(".to", "href") === "#group_b")
    assert(groupElement.firstText(".to") === "group/b")

    val packageElement = actual.one("#group_a--package_c---group_b--package_e")
    assert(packageElement.firstAttr(".from", "href") === "group_a.html#group_a--package_c")
    assert(packageElement.firstText(".from") === "package/c")
    assert(packageElement.firstAttr(".to", "href") === "group_b.html#group_b--package_e")
    assert(packageElement.firstText(".to") === "package/e")

    val classElement = actual.one("#group_a--package_c--class_f---group_b--package_e--class_i")
    assert(classElement.firstAttr(".from", "href") === "group_a--package_c.html#group_a--package_c--class_f")
    assert(classElement.firstText(".from") === "class/f")
    assert(classElement.firstAttr(".to", "href") === "group_b--package_e.html#group_b--package_e--class_i")
    assert(classElement.firstText(".to") === "class/i")
  }
}
