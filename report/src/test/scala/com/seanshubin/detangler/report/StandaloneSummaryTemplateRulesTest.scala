package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class StandaloneSummaryTemplateRulesTest extends FunSuite {
  test("standalone summary") {
    //given
    val standaloneTemplateText =
    """<div class="standalone-summary">
      |  <p class="name">sample/group</p>
      |  <a class="cycle-link" href="">&orarr;</a>
      |  <p class="depth">depth number</p>
      |  <p class="breadth">breadth number</p>
      |  <p class="transitive">transitive number</p>
      |  <p><a class="composed-of" href="">parts</a></p>
      |</div>
    """.stripMargin
    val standaloneSummaryTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneSummaryTemplateRules = new StandaloneSummaryTemplateRulesImpl(SampleData.detangled)
    //when
    val actual = standaloneSummaryTemplateRules.generate(standaloneSummaryTemplate, SampleData.groupA)
    //then
    assert(actual.select(".name").text() === "group/a")
    assert(actual.select(".depth").text() === "1")
    assert(actual.select(".breadth").text() === "1")
    assert(actual.select(".transitive").text() === "1")
    assert(actual.select(".composed-of").text() === "2 parts")
    assert(actual.select(".composed-of").attr("href") === "report--group-a.html")
  }
}
