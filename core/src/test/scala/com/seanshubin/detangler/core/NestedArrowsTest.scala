package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.scalatest.FunSuite

class NestedArrowsTest extends FunSuite {
  val shouldRemoveClass = true
  val jsoupUtil = new JsoupUtil(shouldRemoveClass)
  test("nested arrows") {
    val source = """<html><body><ul class="list"><li class="element content"></li></ul></body></html>"""
    val template = Jsoup.parse(source, "")
    val elementTemplate = jsoupUtil.extractFragment(template, "element")
    val listTemplate = jsoupUtil.extractFragment(template, "list")
    val classArrows: Seq[Arrow] = Seq(Arrow(SampleData.idClassF, SampleData.idClassG, Nil))
    val packageArrows: Seq[Arrow] = Seq(Arrow(SampleData.idPackageC, SampleData.idPackageD, classArrows))
    val groupArrows: Seq[Arrow] = Seq(Arrow(SampleData.idGroupA, SampleData.idGroupB, packageArrows))
    addNestedArrows(template, listTemplate, elementTemplate, groupArrows)
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

  def addNestedArrows(template: Document, listTemplate: Element, elementTemplate: Element, arrows: Seq[Arrow]) = {
    val arrowsList = buildArrowsList(listTemplate, elementTemplate, arrows)
    template.body().appendChild(arrowsList)
  }

  def buildArrowsList(listTemplateOriginal: Element, elementTemplate: Element, arrows: Seq[Arrow]): Element = {
    val arrowElements = arrows.map(arrow => buildArrowElement(listTemplateOriginal, elementTemplate, arrow))
    val listTemplate = listTemplateOriginal.clone()
    arrowElements.foreach(arrowElement => listTemplate.appendChild(arrowElement))
    listTemplate
  }

  def buildArrowElement(listTemplate: Element, elementTemplateOriginal: Element, arrow: Arrow): Element = {
    val elementTemplate = elementTemplateOriginal.clone()
    jsoupUtil.setText(elementTemplate, "content", HtmlUtil.arrowId(arrow.from, arrow.to))
    elementTemplate.appendChild(buildArrowsList(listTemplate, elementTemplateOriginal, arrow.reasons))
    elementTemplate
  }
}
