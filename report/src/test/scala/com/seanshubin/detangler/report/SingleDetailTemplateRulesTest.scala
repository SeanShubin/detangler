package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class SingleDetailTemplateRulesTest extends FunSuite {
  test("single detail") {
    //given
    val singleTemplateText =
      """<div class="single-detail">
        |  <p class="caption">replace-me</p>
        |  <ul class="single-detail-rows">
        |     <li class="single-detail-row">
        |       <p><a class="name" href="replace-me">replace-me</a></p>
        |       <p class="depth">replace-me</p>
        |       <p class="complexity">replace-me</p>
        |       <p><a class="reason" href="replace-me">replace-me</a></p>
        |     </li>
        |  </ul>
        |</div>
      """.stripMargin
    val singleDetailTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleDetailTemplateRules = new SingleDetailTemplateRulesImpl()
    val expected =
      """<div class="single-detail">
        |  <p class="caption">depends on (1)</p>
        |  <ul class="single-detail-rows">
        |     <li class="single-detail-row">
        |       <p><a class="name" href="#group_b">group/b</a></p>
        |       <p class="depth">3</p>
        |       <p class="complexity">4</p>
        |       <p><a class="reason" href="#group_a---group_b">reason</a></p>
        |     </li>
        |  </ul>
        |</div>
      """.stripMargin
    //when
    val actual = singleDetailTemplateRules.generate(singleDetailTemplate, SampleData.groupA, DependencyDirection.TowardDependsOn).text
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
