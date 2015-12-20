package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.SampleData
import org.scalatest.FunSuite

class ReasonsTemplateRulesTest extends FunSuite {
  test("reasons") {
    //given
    val reasonsTemplateText =
      """<div class="reasons append-reason">
        |  <div class="reason append-reasons">
        |    <a class="from" href="">from</a>
        |    <a class="to" href="">to</a>
        |  </div>
        |</div>
        | """.stripMargin
    val reasonsTemplate = HtmlElement.fragmentFromString(reasonsTemplateText)
    val reasonsTemplateRules = new ReasonsTemplateRulesImpl(SampleData.detangled)
    //when
    val actual = reasonsTemplateRules.generate(reasonsTemplate, SampleData.root, SampleData.root)
    //then
    val groupElement = actual.select("#group-a---group-b").remove("#group-a--package-c---group-b--package-e")
    assert(groupElement.select(".from").attr("href") === "#group-a")
    assert(groupElement.select(".from").text() === "group/a")
    assert(groupElement.select(".to").attr("href") === "#group-b")
    assert(groupElement.select(".to").text() === "group/b")

    val packageElement = actual.select("#group-a--package-c---group-b--package-e").remove("#group-a--package-c--class-f---group-b--package-e--class-i")
    assert(packageElement.select(".from").attr("href") === "group-a.html#package-c")
    assert(packageElement.select(".from").text() === "package/c")
    assert(packageElement.select(".to").attr("href") === "group-b.html#package-e")
    assert(packageElement.select(".to").text() === "package/e")

    val classElement = actual.select("#group-a--package-c--class-f---group-b--package-e--class-i")
    assert(classElement.select(".from").attr("href") === "group-a--package-c.html#class-f")
    assert(classElement.select(".from").text() === "class/f")
    assert(classElement.select(".to").attr("href") === "group-b--package-e.html#class-i")
    assert(classElement.select(".to").text() === "class/i")
  }
}
