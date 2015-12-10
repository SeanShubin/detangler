package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class UnitDependencyTemplateTest extends FunSuite {
  val templateText =
    """<ul class="dependency">
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
      |            <tbody class="dependency-row-outer">
      |            <tr class="dependency-row-inner">
      |                <td><a class="name" href="#other_group">other/group</a></td>
      |                <td class="depth">depth</td>
      |                <td class="complexity">complexity</td>
      |                <td><a class="reason" href="other_group_parts">reason</a></td>
      |            </tr>
      |            </tbody>
      |        </table>
      |    </li>
      |</ul>
      | """.stripMargin
  val template = HtmlFragment.fromText(templateText)

  test("unit depends on template") {
    val unit = SampleData.idGroupA
    val unitSummaryTemplate = new UnitDependencyTemplate(template, SampleData.idRoot, unit, ReasonDirection.TowardDependsOn, SampleData.detangled)
    val actual = unitSummaryTemplate.generate()

    assert(actual.text(".caption") === "depends on (1)")
    assert(actual.text(".name") === "group/b")
    assert(actual.attr(".name", "href") === "#group_b")
    assert(actual.text(".depth") === "3")
    assert(actual.text(".complexity") === "4")
    assert(actual.text(".reason") === "reason")
    assert(actual.attr(".reason", "href") === "#group_a---group_b")
  }

  test("unit depended on by on template") {
    val unit = SampleData.idGroupB
    val unitSummaryTemplate = new UnitDependencyTemplate(template, SampleData.idRoot, unit, ReasonDirection.TowardDependedOnBy, SampleData.detangled)
    val actual = unitSummaryTemplate.generate()

    assert(actual.text(".caption") === "depended on by (1)")
    assert(actual.text(".name") === "group/a")
    assert(actual.attr(".name", "href") === "#group_a")
    assert(actual.text(".depth") === "1")
    assert(actual.text(".complexity") === "2")
    assert(actual.text(".reason") === "reason")
    assert(actual.attr(".reason", "href") === "#group_a---group_b")
  }
}
