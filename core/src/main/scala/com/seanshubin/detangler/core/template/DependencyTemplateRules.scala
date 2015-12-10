package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._

class DependencyTemplateRules(dependencyTemplate: HtmlFragment,
                              detangled: Detangled,
                              context: UnitId,
                              parentUnit: UnitId,
                              reasonDirection: ReasonDirection) {
  private val parentTemplate = dependencyTemplate.remove(".dependency-header")
  private val childTemplate = dependencyTemplate.one(".dependency-detail")

  def generate(): HtmlFragment = {
    val childUnits = reasonDirection.dependencies(detangled, context, parentUnit)
    if (childUnits.isEmpty) {
      HtmlFragment.Empty
    } else {
      val rows = childUnits.map(generateRow)
      val result = parentTemplate.
        text(".caption", s"${reasonDirection.caption} (${childUnits.size})").
        appendAll(".dependency-row-outer", rows)
      result
    }
  }

  private def generateRow(childUnitId: UnitId): HtmlFragment = {
    val reasonName = reasonDirection.name(parentUnit, childUnitId)
    val reasonLink = reasonDirection.link(parentUnit, childUnitId)
    childTemplate.
      anchor(".name", HtmlUtil.htmlLink(context, childUnitId), HtmlUtil.htmlName(childUnitId)).
      text(".depth", detangled.depth(childUnitId).toString).
      text(".complexity", detangled.complexity(childUnitId).toString).
      anchor(".reason", reasonLink, reasonName)
  }
}
