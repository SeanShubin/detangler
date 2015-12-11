package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.template.FragmentSelectors._
import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class CycleTemplateRules(cycleTemplate: HtmlFragment, detangled: Detangled, pageModule: Module, module: Module) {
  private val emptyTemplate = cycleTemplate.remove(SelectorCycleSummary).remove(SelectorCycleParts)
  private val summaryTemplate = cycleTemplate.one(SelectorCycleSummary)
  private val partsTemplate = cycleTemplate.one(SelectorCycleParts)

  def generate(): HtmlFragment = {
    val summary = new CycleSummaryTemplateRules(summaryTemplate, detangled, module).generate()
    val parts = new CyclePartsTemplateRules(partsTemplate, detangled, pageModule, module).generate()
    emptyTemplate.appendChild(summary).appendChild(parts)
  }
}
