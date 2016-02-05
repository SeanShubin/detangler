package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class PageTemplateRulesTest extends FunSuite {
  val modulesTemplateRules = new ModulesTemplateRules {
    override def generate(modulesTemplate: HtmlElement, standalone: Standalone): HtmlElement =
      HtmlElement.fragmentFromString( s"""<p class="modules">the modules</p>""")
  }
  val reasonsTemplateRules = new ReasonsTemplateRules {
    override def generate(reasonsTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement = {
      HtmlElement.fragmentFromString( s"""<p class="reasons">the reasons</p>""")
    }
  }
  val pageTemplateText =
    """<a class="graph">graph</a>
      |<a class="parent"/>
      |<p class="modules"/>
      |<p class="reasons"/>
    """.stripMargin
  val pageTemplateRules = new PageTemplateRulesImpl(modulesTemplateRules, reasonsTemplateRules)
  val pageTemplate = HtmlElement.pageFromString(pageTemplateText)

  test("root page template") {
    //when
    val actual = pageTemplateRules.generate(pageTemplate, SampleData.root, isLeafPage = false)
    //then
    assert(actual.select(".modules").text() === "the modules")
    assert(actual.select(".reasons").text() === "the reasons")
  }

  test("page template with parent") {
    //when
    val actual = pageTemplateRules.generate(pageTemplate, SampleData.groupA, isLeafPage = false)
    //then
    assert(actual.select(".parent").text() === "group/a")
    assert(actual.select(".parent").attr("href") === "report.html")
    assert(actual.select(".modules").text() === "the modules")
    assert(actual.select(".reasons").text() === "the reasons")
  }
}
