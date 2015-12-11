package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class ModuleSummaryTemplateTest extends FunSuite {
  /*
    test("module summary template") {
      val templateText =
        """<table class="summary">
          |    <thead>
          |    <tr>
          |        <th>name</th>
          |        <th>depth</th>
          |        <th>complexity</th>
          |        <th>composed of</th>
          |    </tr>
          |    </thead>
          |    <tbody>
          |    <tr>
          |        <td class="name">sample/group</td>
          |        <td class="depth">depth number</td>
          |        <td class="complexity">complexity number</td>
          |        <td><a class="composed-of" href="sample_group_parts">parts</a></td>
          |    </tr>
          |    </tbody>
          |</table>
          | """.stripMargin
      val template = HtmlFragment.fromText(templateText)
      val module = SampleData.idGroupA
      val moduleSummaryTemplate = new ModuleSummaryTemplate(template, SampleData.detangled)
      val actual = moduleSummaryTemplate.generate(module)

      assert(actual.attr(".summary", "id") === "group_a")
      assert(actual.text(".name") === "group/a")
      assert(actual.text(".depth") === "1")
      assert(actual.text(".complexity") === "2")
      assert(actual.text(".composed-of") === "parts")
      assert(actual.attr(".composed-of", "href") === "group_a.html")
    }
  */
}
