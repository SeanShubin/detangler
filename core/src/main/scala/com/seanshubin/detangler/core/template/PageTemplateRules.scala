package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, UnitId}

class PageTemplateRules(pageTemplate: HtmlFragment, detangled: Detangled, unit: UnitId) {
  private val emptyTemplate = pageTemplate.remove(".units").remove(".reasons")
  private val unitsTemplate = pageTemplate.one(".units")
  private val reasonsTemplate = pageTemplate.one(".reasons")

  def generate(): HtmlFragment = {
    val units = new UnitsTemplateRules(unitsTemplate, detangled, unit).generate()
    val arrows = detangled.arrowsFor(unit)
    val reasons = new ReasonsTemplateRules(reasonsTemplate, detangled, unit, arrows).generate()
    emptyTemplate.appendChild(units).appendChild(reasons)
  }
}
