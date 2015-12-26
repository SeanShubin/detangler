package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Standalone}
import org.scalatest.FunSuite

class ModulesTemplateRulesTest extends FunSuite {
  val standaloneTemplateRules = new StandaloneTemplateRules {
    override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>${standalone.toString}</p>")
  }
  val cycleTemplateRules = new CycleTemplateRules {
    override def generate(standaloneTemplate: HtmlElement, context: Standalone, cycle: Cycle): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>${cycle.toString}</p>")
  }
  val modulesTemplateText =
    """<div class="append-module">
      |  <div class="standalone">
      |  </div>
      |  <div class="cycle">
      |  </div>
      |</div>
    """.stripMargin
  val modulesTemplate = HtmlElement.fragmentFromString(modulesTemplateText)

  test("modules template without cycles") {
    //given
    val modulesTemplateRules = new ModulesTemplateRulesImpl(standaloneTemplateRules, cycleTemplateRules, SampleData.detangled)
    val expected =
      """<div class="append-module">
        |  <p>group/a</p>
        |  <p>group/b</p>
        |</div>
      """.stripMargin
    //when
    val actual = modulesTemplateRules.generate(modulesTemplate, SampleData.root).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }

  test("modules template with cycles") {
    //given
    val modulesTemplateRules = new ModulesTemplateRulesImpl(standaloneTemplateRules, cycleTemplateRules, SampleDataWithCycles.detangled)
    val expected =
      """<div class="append-module">
        |  <p>group/a-group/b</p>
        |  <p>group/a</p>
        |  <p>group/b</p>
        |</div>
      """.stripMargin
    //when
    val actual = modulesTemplateRules.generate(modulesTemplate, SampleDataWithCycles.root).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
