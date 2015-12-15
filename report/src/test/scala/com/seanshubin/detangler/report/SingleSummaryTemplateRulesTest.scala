package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class SingleSummaryTemplateRulesTest extends FunSuite {
  test("single summary") {
    //given
    val singleTemplateText =
      """<div class="single-summary">
        |  <p class="name">sample/group</p>
        |  <p class="depth">depth number</p>
        |  <p class="complexity">complexity number</p>
        |  <p><a class="composed-of" href="">parts</a></p>
        |</div>
      """.stripMargin
    val singleSummaryTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleSummaryTemplateRules = new SingleSummaryTemplateRulesImpl(SampleData.detangled)
    val expected =
      """<div class="single-summary" id="group-a">
        |  <p class="name">group/a</p>
        |  <p class="depth">1</p>
        |  <p class="complexity">1</p>
        |  <p><a class="composed-of" href="group-a.html">parts</a></p>
        |</div>
      """.stripMargin
    //when
    val actual = singleSummaryTemplateRules.generate(singleSummaryTemplate, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
