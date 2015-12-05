package com.seanshubin.detangler.core

class UnitDependencyTemplate(templateText: String, detangled:Detangled) {
  private val original = HtmlFragment.fromText(templateText)
  private val innerTemplate = original.one(".unit-dependency-row-inner")
  private val outerTemplate = original.delete(".unit-dependency-row-inner")

  def generate(unit:UnitId): String = {
    /*
                    <tbody class="attach-unit-dependency-row">
                    <tr class="unit-dependency-row">
                        <td><a class="name" href="#other_group">other/group</a></td>
                        <td class="depth">3</td>
                        <td class="complexity">4</td>
                        <td><a class="reason" href="other_group_parts">reason</a></td>
                    </tr>
                    </tbody>

    jsoupUtil.setText(unitDetailList, "caption", s"$caption ($size)")
        val (arrowName, arrowLink) = if (arrowDirection) {
      (HtmlUtil.arrowName(from, unitId), HtmlUtil.arrowLink(from, unitId))
    } else {
      (HtmlUtil.arrowName(unitId, from), HtmlUtil.arrowLink(unitId, from))
    }
    val unitDependsOnRow = unitDependsOnRowOriginal.clone()
    jsoupUtil.setAnchor(unitDependsOnRow, "name", HtmlUtil.htmlName(unitId), HtmlUtil.htmlLink(pageUnitId, unitId))
    jsoupUtil.setText(unitDependsOnRow, "depth", detangled.depth(unitId).toString)
    jsoupUtil.setText(unitDependsOnRow, "complexity", detangled.complexity(unitId).toString)
    jsoupUtil.setAnchor(unitDependsOnRow, "reason", arrowName, arrowLink)

     */
    val dependencyUnits = detangled.dependsOn(unit)

    outerTemplate.
      text(".caption", s"depends on (${dependencyUnits.size})").
      text
  }
}
