package com.seanshubin.detangler.core

class UnitDependencyTemplate(templateText: String,
                             pageUnitId: UnitId,
                             parentUnitId: UnitId,
                             arrowDirection: ArrowDirection,
                             detangled: Detangled) {
  private val originalTemplate = HtmlFragment.fromText(templateText)
  private val parentTemplate = originalTemplate.remove(".unit-dependency-row-inner")
  private val childTemplate = originalTemplate.one(".unit-dependency-row-inner")

  def generate(): HtmlFragment = {
    val childUnits = arrowDirection.dependencies(detangled, parentUnitId)
    val rows = childUnits.map(generateRow)

    val result = parentTemplate.
      text(".caption", s"${arrowDirection.caption} (${childUnits.size})").
      appendAll(".unit-dependency-row-outer", rows)
    result
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
