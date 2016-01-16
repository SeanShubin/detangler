package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Module, Standalone}
import org.scalatest.FunSuite

class StandaloneTemplateRulesTest extends FunSuite {
  val standaloneSummaryTemplateRules = new StandaloneSummaryTemplateRules {
    override def generate(standaloneTemplate: HtmlElement, standalone: Standalone): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>summary ${standalone.toString}</p>")
  }
  val standaloneTemplateText =
    """<div class="standalone">
      |  <div class="standalone-summary"></div>
      |  <div class="standalone-dependency"></div>
      |  <div class="standalone-depends-on"></div>
      |  <div class="standalone-depended-on-by"></div>
      |</div>
    """.stripMargin

  test("standalone") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${standalone.toString}</p>")
        Some(element)
      }
    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        Some(element)
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary group/a</p>
        |  <p>depends on group/a</p>
        |  <p>depended on by group/a</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }

  test("no depends on") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        None
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        Some(element)
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary group/a</p>
        |  <p>depended on by group/a</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }

  test("no depended on by") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${standalone.toString}</p>")
        Some(element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        None
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary group/a</p>
        |  <p>depends on group/a</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }

  test("no dependencies") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        None
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Module): Option[HtmlElement] = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        None
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary group/a</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n", "\n", "\n"))
  }
}
