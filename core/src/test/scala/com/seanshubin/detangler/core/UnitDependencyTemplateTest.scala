package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class UnitDependencyTemplateTest extends FunSuite {
  test("unit detail template") {
    val templateText =
      """<ul class="unit-dependency">
        |    <li>
        |        <table>
        |            <thead>
        |            <tr>
        |                <th class="caption" colspan="4">depends on (number)</th>
        |            </tr>
        |            <tr>
        |                <th>name</th>
        |                <th>depth</th>
        |                <th>complexity</th>
        |                <th>reason</th>
        |            </tr>
        |            </thead>
        |            <tbody class="unit-dependency-row-outer">
        |            <tr class="unit-dependency-row-inner">
        |                <td><a class="name" href="#other_group">other/group</a></td>
        |                <td class="depth">3</td>
        |                <td class="complexity">4</td>
        |                <td><a class="reason" href="other_group_parts">reason</a></td>
        |            </tr>
        |            </tbody>
        |        </table>
        |    </li>
        |</ul>
        |""".stripMargin
    val unit = SampleData.idGroupA
    val unitSummaryTemplate = new UnitDependencyTemplate(templateText, SampleData.detangled)
    val replacedText = unitSummaryTemplate.generate(unit)
    val actual = HtmlFragment.fromText(replacedText)

    assert(actual.text(".caption") === "depends on (1)")
    assert(actual.text(".name") === "group/b")
    assert(actual.attr(".name", "href") === "#group_b")
    assert(actual.text(".depth") === "3")
    assert(actual.text(".complexity") === "4")
    assert(actual.text(".reason") === "reason")
    assert(actual.attr(".reason", "href") === "#group_a---group_b")
  }
}
