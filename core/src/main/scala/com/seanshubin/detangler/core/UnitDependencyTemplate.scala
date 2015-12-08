package com.seanshubin.detangler.core

class UnitDependencyTemplate(template: HtmlFragment,
                             pageUnitId: UnitId,
                             parentUnitId: UnitId,
                             arrowDirection: ArrowDirection,
                             detangled: Detangled) {
  private val parentTemplate = template.remove(".dependency-row-inner")
  private val childTemplate = template.one(".dependency-row-inner")

  def generate(): HtmlFragment = {
    val childUnits = arrowDirection.dependencies(detangled, pageUnitId, parentUnitId)
    if (childUnits.isEmpty) {
      HtmlFragment.Empty
    } else {
      val rows = childUnits.map(generateRow)
      val result = parentTemplate.
        text(".caption", s"${arrowDirection.caption} (${childUnits.size})").
        appendAll(".dependency-row-outer", rows)
      result
    }
  }

  private def generateRow(childUnitId: UnitId): HtmlFragment = {
    val arrowName = arrowDirection.arrowName(parentUnitId, childUnitId)
    val arrowLink = arrowDirection.arrowLink(parentUnitId, childUnitId)
    childTemplate.
      anchor(".name", HtmlUtil.htmlLink(pageUnitId, childUnitId), HtmlUtil.htmlName(childUnitId)).
      text(".depth", detangled.depth(childUnitId).toString).
      text(".complexity", detangled.complexity(childUnitId).toString).
      anchor(".reason", arrowLink, arrowName)
  }
}
