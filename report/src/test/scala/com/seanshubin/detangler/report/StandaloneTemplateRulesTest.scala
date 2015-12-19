package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone
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
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${standalone.toString}</p>")
        QuantityAndElement(1, element)
      }
    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        QuantityAndElement(1, element)
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary Standalone(group/a)</p>
        |  <p>depends on Standalone(group/a)</p>
        |  <p>depended on by Standalone(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("no depends on") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${standalone.toString}</p>")
        QuantityAndElement(0, element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        QuantityAndElement(1, element)
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary Standalone(group/a)</p>
        |  <p>depended on by Standalone(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("no depended on by") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${standalone.toString}</p>")
        QuantityAndElement(1, element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        QuantityAndElement(0, element)
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary Standalone(group/a)</p>
        |  <p>depends on Standalone(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("no dependencies") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${standalone.toString}</p>")
        QuantityAndElement(0, element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${standalone.toString}</p>")
        QuantityAndElement(0, element)
      }
    }
    val standaloneTemplate = HtmlElement.fragmentFromString(standaloneTemplateText)
    val standaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="standalone">
        |  <p>summary Standalone(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = standaloneTemplateRules.generate(standaloneTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }
}