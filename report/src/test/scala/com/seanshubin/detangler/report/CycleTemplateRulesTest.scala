package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class CycleTemplateRulesTest extends FunSuite {
  val standaloneSummaryTemplateRules = new StandaloneSummaryTemplateRules {
    override def generate(standaloneTemplate: HtmlElement, standalone: Standalone): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>summary ${standalone.toString}</p>")
  }

  test("cycle") {
    //given
    val detangled = SampleDataWithCycles.detangled
    val cycleTemplateText =
      """<div class="cycle">
        |  <div class="cycle-summary">
        |    <p class="size">size number</p>
        |    <p class="depth">depth number</p>
        |    <p class="complexity">0</p>
        |  </div>
        |  <div class="cycle-detail">
        |    <div class="cycle-parts append-cycle-part">
        |      <div class="cycle-part">
        |        <p><a class="name" href="">other/group</a></p>
        |      </div>
        |    </div>
        |  </div>
        |</div>
      """.stripMargin
    val cycleTemplate = HtmlElement.fragmentFromString(cycleTemplateText)
    val cycleTemplateRules = new CycleTemplateRulesImpl(detangled)
    val expected =
      """<div class="cycle">
        |  <div class="cycle-summary">
        |    <p class="size">2</p>
        |    <p class="depth">0</p>
        |    <p class="complexity">0</p>
        |  </div>
        |  <div class="cycle-detail">
        |    <div class="cycle-parts append-cycle-part">
        |      <div class="cycle-part">
        |        <p><a class="name" href="#group-a">group/a</a></p>
        |      </div>
        |      <div class="cycle-part">
        |        <p><a class="name" href="#group-b">group/b</a></p>
        |      </div>
        |    </div>
        |  </div>
        |</div>
        | """.stripMargin
    //when
    val actual = cycleTemplateRules.generate(cycleTemplate, detangled.root(), SampleDataWithCycles.cycleAB).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
