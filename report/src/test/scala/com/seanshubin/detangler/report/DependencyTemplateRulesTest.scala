package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class DependencyTemplateRulesTest extends FunSuite {
  test("single detail") {
    //given
    val singleTemplateText =
      """<div class="single-dependency">
        |  <p class="caption">replace-me</p>
        |  <ul class="single-append-dependency-row">
        |     <li class="single-dependency-row">
        |       <p><a class="name" href="replace-me">replace-me</a></p>
        |       <p class="depth">replace-me</p>
        |       <p class="complexity">replace-me</p>
        |       <p><a class="reason" href="replace-me">replace-me</a></p>
        |     </li>
        |  </ul>
        |</div>
      """.stripMargin
    val singleDetailTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleDetailTemplateRules = new DependencyTemplateRulesImpl(
      SampleData.detangled, DependencyDirection.TowardDependsOn)
    //when
    val QuantityAndElement(quantity, actual) = singleDetailTemplateRules.generate(singleDetailTemplate, SampleData.theRoot, SampleData.groupA)
    //then
    assert(quantity === 1)
    assert(actual.select(".caption").text() === "depends on (1)")
    assert(actual.select(".name").attr("href") === "#group-b")
    assert(actual.select(".name").text() === "group/b")
    assert(actual.select(".depth").text() === "0")
    assert(actual.select(".complexity").text() === "0")
    assert(actual.select(".reason").attr("href") === "#group-a---group-b")
    assert(actual.select(".reason").text() === "reason")
  }
}
