package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single
import org.scalatest.FunSuite

class PageTemplateRulesTest extends FunSuite {
  test("page template") {
    //given
    val modulesTemplateRules = new ModulesTemplateRules {
      override def generate(modulesTemplate: HtmlElement, single: Single): HtmlElement =
        HtmlElement.fragmentFromString(s"<p>modules for $single</p>")
    }
    val reasonsTemplateRules = new ReasonsTemplateRules {
      override def generate(reasonsTemplate: HtmlElement, single: Single): HtmlElement =
        HtmlElement.fragmentFromString(s"<p>reasons for $single</p>")
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
        |    <p>modules for Single()</p>
        |    <p>reasons for Single()</p>
        |  </body>
        |</html>
        | """.stripMargin
    //when
    val actual = pageTemplateRules.generate(pageTemplate, SampleData.moduleRoot)
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }
}
