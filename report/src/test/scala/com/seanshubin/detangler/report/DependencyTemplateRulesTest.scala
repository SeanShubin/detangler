package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.SampleData
import org.scalatest.FunSuite

class DependencyTemplateRulesTest extends FunSuite {
  test("standalone detail") {
    //given
    val standaloneTemplateText =
      """<div class="standalone-dependency">
        |  <p class="caption">replace-me</p>
        |  <ul class="standalone-append-dependency-row">
        |     <li class="standalone-dependency-row">
        |       <p><a class="name" href="replace-me">replace-me</a></p>
        |       <p class="depth">replace-me</p>
        |       <p class="complexity">replace-me</p>
        |       <p><a class="reason" href="replace-me">replace-me</a></p>
        |     </li>
        |  </ul>
        |</div>
      """.stripMargin
    val standaloneDetailTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneDetailTemplateRules = new DependencyTemplateRulesImpl(
      SampleData.detangled, DependencyDirection.TowardDependsOn)
    //when
    val QuantityAndElement(quantity, actual) = standaloneDetailTemplateRules.generate(standaloneDetailTemplate, SampleData.root, SampleData.groupA)
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
