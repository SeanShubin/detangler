package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single
import org.scalatest.FunSuite

class CycleTemplateRulesTest extends FunSuite {
  val singleSummaryTemplateRules = new SingleSummaryTemplateRules {
    override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>summary ${single.toString}</p>")
  }

  test("cycle") {
    //given
    val detangled = SampleDataWithCycles.detangled
    val cycleTemplateText =
      """<div class="cycle">
        |  <div class="cycle-summary">
        |  </div>
        |  <div class="cycle-detail">
        |  </div>
        |</div>
      """.stripMargin
    val cycleTemplate = HtmlElement.fragmentFromString(cycleTemplateText)
    val cycleTemplateRules = new CycleTemplateRulesImpl(detangled)
    val expected =
      """""".stripMargin
    //when
    val actual = cycleTemplateRules.generate(cycleTemplate, detangled.root(), SampleDataWithCycles.cycleAB).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n","\n","\n"))
  }
}
