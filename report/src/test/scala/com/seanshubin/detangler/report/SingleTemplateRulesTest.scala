package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single
import org.scalatest.FunSuite

class SingleTemplateRulesTest extends FunSuite {
  val singleSummaryTemplateRules = new SingleSummaryTemplateRules {
    override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement =
      HtmlElement.fragmentFromString(s"<p>summary ${single.toString}</p>")
  }
  val singleTemplateText =
    """<div class="single">
      |  <div class="single-summary"></div>
      |  <div class="single-dependency"></div>
      |  <div class="single-depends-on"></div>
      |  <div class="single-depended-on-by"></div>
      |</div>
    """.stripMargin

  test("single") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${single.toString}</p>")
        QuantityAndElement(1, element)
      }
    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${single.toString}</p>")
        QuantityAndElement(1, element)
      }
    }
    val singleTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleTemplateRules = new SingleTemplateRulesImpl(singleSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="single">
        |  <p>summary Single(group/a)</p>
        |  <p>depends on Single(group/a)</p>
        |  <p>depended on by Single(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = singleTemplateRules.generate(singleTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("no depends on") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${single.toString}</p>")
        QuantityAndElement(0, element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${single.toString}</p>")
        QuantityAndElement(1, element)
      }
    }
    val singleTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleTemplateRules = new SingleTemplateRulesImpl(singleSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="single">
        |  <p>summary Single(group/a)</p>
        |  <p>depended on by Single(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = singleTemplateRules.generate(singleTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("no depended on by") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${single.toString}</p>")
        QuantityAndElement(1, element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${single.toString}</p>")
        QuantityAndElement(0, element)
      }
    }
    val singleTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleTemplateRules = new SingleTemplateRulesImpl(singleSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="single">
        |  <p>summary Single(group/a)</p>
        |  <p>depends on Single(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = singleTemplateRules.generate(singleTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }

  test("no dependencies") {
    //given
    val dependsOnTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depends on ${single.toString}</p>")
        QuantityAndElement(0, element)
      }

    }
    val dependedOnByTemplateRules = new DependencyTemplateRules {
      override def generate(singleTemplate: HtmlElement, context: Single, single: Single): QuantityAndElement = {
        val element = HtmlElement.fragmentFromString(s"<p>depended on by ${single.toString}</p>")
        QuantityAndElement(0, element)
      }
    }
    val singleTemplate = HtmlElement.fragmentFromString(singleTemplateText)
    val singleTemplateRules = new SingleTemplateRulesImpl(singleSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val expected =
      """<div class="single">
        |  <p>summary Single(group/a)</p>
        |</div>
      """.stripMargin
    //when
    val actual = singleTemplateRules.generate(singleTemplate, SampleData.root, SampleData.groupA).toString
    //then
    val linesCompareResult = LinesDifference.compare(actual, expected)
    assert(linesCompareResult.isSame, linesCompareResult.detailLines.mkString("\n"))
  }
}
