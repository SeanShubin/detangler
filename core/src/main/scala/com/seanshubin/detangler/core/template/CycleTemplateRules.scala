package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.template.FragmentSelectors._
import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class CycleTemplateRules(cycleTemplate: HtmlFragment, detangled: Detangled, pageModule: Module, module: Module) {
  private val emptyTemplate = cycleTemplate.remove(SelectorCycleSummary).remove(SelectorCycleDetail)
  private val summaryTemplate = cycleTemplate.one(SelectorCycleSummary)
  private val detailTemplate = cycleTemplate.one(SelectorCycleDetail)

  def generate(): HtmlFragment = {
    val summary = new CycleSummaryTemplateRules(summaryTemplate, detangled, module).generate()
    val detail = new CycleDetailTemplateRules(detailTemplate, detangled, pageModule, module).generate()
    val result = emptyTemplate.appendChild(summary).appendChild(detail)
    result
  }
}
