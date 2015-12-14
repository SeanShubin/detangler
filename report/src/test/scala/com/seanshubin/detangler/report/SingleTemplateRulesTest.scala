package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single
import org.scalatest.FunSuite

class SingleTemplateRulesTest extends FunSuite {
  test("single") {
    //given
    val singleSummaryTemplateRules = new SingleSummaryTemplateRules {
      override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement =
        HtmlElement.fragmentFromString(s"<p>summary ${single.toString}</p>")
    }
    val singleDetailTemplateRules = new SingleDetailTemplateRules {
      override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement =
        HtmlElement.fragmentFromString(s"<p>detail ${single.toString}</p>")
    }
    val singleTemplateText =
      """<div class="single">
        |  <div class="summary">
        |  </div>
        |  <div class="detail">
        |  </div>
        |</div>
      """.stripMargin
    val singleTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleTemplateRules = new SingleTemplateRulesImpl(singleSummaryTemplateRules, singleDetailTemplateRules)
    val expected =
      """<div class="single">
        |  <p>summary Single(group/a)</p>
        |  <p>detail Single(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = singleTemplateRules.generate(singleTemplate, SampleData.groupA).text
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }
}
