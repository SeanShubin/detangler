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
        |    <p class="breadth">breadth number</p>
        |    <p class="transitive">transitive number</p>
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
    //when
    val actual = cycleTemplateRules.generate(cycleTemplate, Standalone.Root, SampleDataWithCycles.cycleAB)
    //then
    assert(actual.select(".size").text() === "2")
    assert(actual.select(".depth").text() === "0")
    assert(actual.select(".breadth").text() === "0")
    assert(actual.select(".transitive").text() === "0")
    val cycleParts = actual.selectAll(".cycle-part")
    assert(cycleParts.size === 2)
    assert(cycleParts(0).select(".name").text() === "group/a")
    assert(cycleParts(0).select(".name").attr("href") === "#group-a")
    assert(cycleParts(1).select(".name").text() === "group/b")
    assert(cycleParts(1).select(".name").attr("href") === "#group-b")
  }
}
