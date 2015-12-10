package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class CycleTemplateRules(cycleTemplate: HtmlFragment, detangled: Detangled, pageModule: Module, module: Module) {
  private val emptyTemplate = cycleTemplate.remove(".summary").remove(".parts")
  private val summaryTemplate = cycleTemplate.one(".summary")
  private val partsTemplate = cycleTemplate.one(".parts")

  def generate(): HtmlFragment = {
    val summary = new CycleSummaryTemplateRules(summaryTemplate, detangled, module).generate()
    val parts = new CyclePartsTemplateRules(partsTemplate, detangled, pageModule, module).generate()
    emptyTemplate.appendChild(summary).appendChild(parts)
  }
}
