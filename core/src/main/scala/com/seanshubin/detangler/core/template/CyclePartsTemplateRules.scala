package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, HtmlUtil, UnitId}

class CyclePartsTemplateRules(partsTemplate: HtmlFragment, detangled: Detangled, pageUnit: UnitId, unit: UnitId) {
  private val parentTemplate = partsTemplate.remove(".part")
  private val childTemplate = partsTemplate.one(".part")

  def generate(): HtmlFragment = {
    val children = detangled.composedOf(unit).map(generateChild)
    parentTemplate.appendAll(children)
  }

  private def generateChild(child: UnitId): HtmlFragment = {
    childTemplate.anchor(HtmlUtil.htmlLink(pageUnit, child), HtmlUtil.htmlName(unit))
  }
}
