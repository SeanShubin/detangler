package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class StandaloneSummaryTemplateRulesTest extends FunSuite {
  test("standalone summary") {
    //given
    val standaloneTemplateText =
      """<div class="standalone-summary">
        |  <p class="name">sample/group</p>
        |  <p class="depth">depth number</p>
        |  <p class="complexity">complexity number</p>
        |  <p><a class="composed-of" href="">parts</a></p>
        |</div>
      """.stripMargin
    val standaloneSummaryTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneSummaryTemplateRules = new StandaloneSummaryTemplateRulesImpl(SampleData.detangled)
    val expected =
      """<div class="standalone-summary" id="group-a">
        |  <p class="name">group/a</p>
        |  <p class="depth">1</p>
        |  <p class="complexity">1</p>
        |  <p><a class="composed-of" href="group-a.html">parts</a></p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneSummaryTemplateRules.generate(standaloneSummaryTemplate, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
