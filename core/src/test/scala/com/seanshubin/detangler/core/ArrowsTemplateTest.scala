package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.scalatest.FunSuite

class ArrowsTemplateTest extends FunSuite {

  import JsoupUtil.exactlyOneElement
  import JsoupUtil.firstElement

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
    val arrows =
      Seq(Arrow(SampleData.idGroupA, SampleData.idGroupB,
        Seq(Arrow(SampleData.idPackageC, SampleData.idPackageE,
          Seq(Arrow(SampleData.idClassF, SampleData.idClassI, Seq()))))))
    val replacedText = TemplateUtil.arrows(templateText, arrows)
    val replacedDocument = Jsoup.parse(replacedText)

    val groupElement = exactlyOneElement(replacedDocument, "#group_a---group_b")
    assert(firstElement(groupElement,".from").attr("href") === "#group_a")
    assert(firstElement(groupElement,".from").text() === "group/a")
    assert(firstElement(groupElement,".to").attr("href") === "#group_b")
    assert(firstElement(groupElement,".to").text() === "group/b")

    val packageElement = exactlyOneElement(replacedDocument, "#group_a--package_c---group_b--package_e")
    assert(firstElement(packageElement,".from").attr("href") === "group_a.html#group_a--package_c")
    assert(firstElement(packageElement,".from").text() === "package/c")
    assert(firstElement(packageElement,".to").attr("href") === "group_b.html#group_b--package_e")
    assert(firstElement(packageElement,".to").text() === "package/e")

    val classElement = exactlyOneElement(replacedDocument, "#group_a--package_c--class_f---group_b--package_e--class_i")
    assert(firstElement(classElement,".from").attr("href") === "group_a--package_c.html#group_a--package_c--class_f")
    assert(firstElement(classElement,".from").text() === "class/f")
    assert(firstElement(classElement,".to").attr("href") === "group_b--package_e.html#group_b--package_e--class_i")
    assert(firstElement(classElement,".to").text() === "class/i")
  }
}
