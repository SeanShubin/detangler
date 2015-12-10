package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.scalatest.FunSuite

class NestedReasonsTest extends FunSuite {
  val shouldRemoveClass = true
  val jsoupUtil = new JsoupUtil(shouldRemoveClass)
  test("nested reasons") {
    val source = """<html><body><ul class="list"><li class="element content"></li></ul></body></html>"""
    val template = Jsoup.parse(source, "")
    val elementTemplate = jsoupUtil.extractFragment(template, "element")
    val listTemplate = jsoupUtil.extractFragment(template, "list")
    val classReasons: Seq[Reason] = Seq(Reason(SampleData.idClassF, SampleData.idClassG, Nil))
    val packageReasons: Seq[Reason] = Seq(Reason(SampleData.idPackageC, SampleData.idPackageD, classReasons))
    val groupReasons: Seq[Reason] = Seq(Reason(SampleData.idGroupA, SampleData.idGroupB, packageReasons))
    addNestedReasons(template, listTemplate, elementTemplate, groupReasons)
    template.outputSettings().indentAmount(2)
    val actual = template.toString
    val expected =
      """<html>
        |  <head></head>
        |  <body>
        |    <ul>
        |      <li>group_a---group_b
        |        <ul>
        |          <li>group_a--package_c---group_a--package_d
        |            <ul>
        |              <li>group_a--package_c--class_f---group_a--package_c--class_g
        |                <ul></ul></li>
        |            </ul></li>
        |        </ul></li>
        |    </ul>
        |  </body>
        |</html>""".stripMargin
    val linesDifference = LinesDifference.compare(actual, expected)
    assert(linesDifference.isSame, linesDifference.detailLines.mkString("\n"))
  }

  def addNestedReasons(template: Document, listTemplate: Element, elementTemplate: Element, reasons: Seq[Reason]) = {
    val reasonsList = buildReasonsList(listTemplate, elementTemplate, reasons)
    template.body().appendChild(reasonsList)
  }

  def buildReasonsList(listTemplateOriginal: Element, elementTemplate: Element, reasons: Seq[Reason]): Element = {
    val reasonElements = reasons.map(reason => buildReasonElement(listTemplateOriginal, elementTemplate, reason))
    val listTemplate = listTemplateOriginal.clone()
    reasonElements.foreach(reasonElement => listTemplate.appendChild(reasonElement))
    listTemplate
  }

  def buildReasonElement(listTemplate: Element, elementTemplateOriginal: Element, reason: Reason): Element = {
    val elementTemplate = elementTemplateOriginal.clone()
    jsoupUtil.setText(elementTemplate, "content", HtmlUtil.reasonId(reason.from, reason.to))
    elementTemplate.appendChild(buildReasonsList(listTemplate, elementTemplateOriginal, reason.reasons))
    elementTemplate
  }
}
