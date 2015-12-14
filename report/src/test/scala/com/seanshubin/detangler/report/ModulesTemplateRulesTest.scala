package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Single}
import org.scalatest.FunSuite

class ModulesTemplateRulesTest extends FunSuite {
  val singleTemplateRules = new SingleTemplateRules {
    override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>${single.toString}</p>")
  }
  val cycleTemplateRules = new CycleTemplateRules {
    override def generate(singleTemplate: HtmlElement, cycle: Cycle): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>${cycle.toString}</p>")
  }
  val modulesTemplateText =
    """<div class="module-append-here">
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
      """<div class="module-append-here">
        |  <p>Single(group/a)</p>
        |  <p>Single(group/b)</p>
        |</div>
      """.stripMargin
    //when
    val actual = modulesTemplateRules.generate(modulesTemplate, SampleData.moduleRoot).text
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("modules template with cycles") {
    //given
    val modulesTemplateRules = new ModulesTemplateRulesImpl(singleTemplateRules, cycleTemplateRules, SampleDataWithCycles.detangled)
    val expected =
      """<div class="module-append-here">
        |  <p>Cycle(Single(group/a)--Single(group/b))</p>
        |  <p>Single(group/a)</p>
        |  <p>Single(group/b)</p>
        |</div>
      """.stripMargin
    //when
    val actual = modulesTemplateRules.generate(modulesTemplate, SampleDataWithCycles.moduleRoot).text
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }
}
