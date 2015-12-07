package com.seanshubin.detangler.core

class UnitDependencyTemplate(templateText: String,
                             parentUnitId:UnitId,
                             caption:String,
                             dependencyFunction:UnitId => Seq[UnitId],
                             detangled:Detangled) {
  private val originalTemplate = HtmlFragment.fromText(templateText)
  private val parentTemplate = originalTemplate.delete(".unit-dependency-row-inner")
  private val childTemplate = originalTemplate.one(".unit-dependency-row-inner")

  def generate(): String = {
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
    val childUnits = dependencyFunction(parentUnitId)
    val rows = childUnits.map(generateRow)

    println("-" * 100)
    println(parentTemplate.text)
    println("-" * 100)
    println(childTemplate.text)
    println("-" * 100)

    parentTemplate.
      text(".caption", s"$caption (${childUnits.size})").
      appendAll(".unit-dependency-row-outer", rows).
      text
  }

  private def generateRow(childUnitId:UnitId):HtmlFragment = {
    childTemplate
  }
}
