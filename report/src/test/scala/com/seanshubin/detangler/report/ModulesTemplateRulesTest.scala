package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Single}
import org.scalatest.FunSuite

class ModulesTemplateRulesTest extends FunSuite {
  val singleTemplateRules = new SingleTemplateRules {
    override def generate(singleTemplate: HtmlElement, context: Single, single: Single): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>${single.toString}</p>")
  }
  val cycleTemplateRules = new CycleTemplateRules {
    override def generate(singleTemplate: HtmlElement, cycle: Cycle): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>${cycle.toString}</p>")
  }
  val modulesTemplateText =
    """<div class="append-module">
      |  <div class="single">
      |  </div>
      |  <div class="cycle">
      |  </div>
      |</div>
    """.stripMargin
  val modulesTemplate = HtmlElement.fragmentFromString(modulesTemplateText)

  test("modules template without cycles") {
    //given
    val modulesTemplateRules = new ModulesTemplateRulesImpl(singleTemplateRules, cycleTemplateRules, SampleData.detangled)
    val expected =
      """<div class="append-module">
        |  <p>Single(group/a)</p>
        |  <p>Single(group/b)</p>
        |</div>
      """.stripMargin
    //when
    val actual = modulesTemplateRules.generate(modulesTemplate, SampleData.root).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("modules template with cycles") {
    //given
    val modulesTemplateRules = new ModulesTemplateRulesImpl(singleTemplateRules, cycleTemplateRules, SampleDataWithCycles.detangled)
    val expected =
      """<div class="append-module">
        |  <p>Cycle(Single(group/a)--Single(group/b))</p>
        |  <p>Single(group/a)</p>
        |  <p>Single(group/b)</p>
        |</div>
      """.stripMargin
    //when
    val actual = modulesTemplateRules.generate(modulesTemplate, SampleDataWithCycles.root).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }
}
