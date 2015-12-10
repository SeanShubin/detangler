package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, UnitId}

class CycleTemplateRules(cycleTemplate: HtmlFragment, detangled: Detangled, pageUnit: UnitId, unit: UnitId) {
  private val emptyTemplate = cycleTemplate.remove(".summary").remove(".parts")
  private val summaryTemplate = cycleTemplate.one(".summary")
  private val partsTemplate = cycleTemplate.one(".parts")

  def generate(): HtmlFragment = {
    val summary = new CycleSummaryTemplateRules(summaryTemplate, detangled, unit).generate()
    val parts = new CyclePartsTemplateRules(partsTemplate, detangled, pageUnit, unit).generate()
    emptyTemplate.appendChild(summary).appendChild(parts)
  }
}
