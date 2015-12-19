package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class PageTemplateRulesTest extends FunSuite {
  test("page template") {
    //given
    val modulesTemplateRules = new ModulesTemplateRules {
      override def generate(modulesTemplate: HtmlElement, standalone: Standalone): HtmlElement =
        HtmlElement.fragmentFromString(s"<p>modules for $standalone</p>")
    }
    val reasonsTemplateRules = new ReasonsTemplateRules {
      override def generate(reasonsTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement = {
        HtmlElement.fragmentFromString(s"<p>reasons for $standalone</p>")
      }
    }
    val pageTemplateRules = new PageTemplateRulesImpl(modulesTemplateRules, reasonsTemplateRules)
    val pageTemplateText =
      """<div class="modules">
        |</div>
        |<div class="reasons">
        |</div>
      """.stripMargin
    val pageTemplate = HtmlElement.pageFromString(pageTemplateText)
    val expected =
      """<html>
        |  <head></head>
        |  <body>
        |    <p>modules for Standalone()</p>
        |    <p>reasons for Standalone()</p>
        |  </body>
        |</html>
        | """.stripMargin
    //when
    val actual = pageTemplateRules.generate(pageTemplate, SampleData.root)
    //then
    val linesCompareResult = LinesDifference.compare(actual.toString, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
