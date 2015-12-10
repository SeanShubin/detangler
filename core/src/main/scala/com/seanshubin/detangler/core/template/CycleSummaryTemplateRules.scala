package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class CycleSummaryTemplateRules(summaryTemplate: HtmlFragment, detangled: Detangled, module: Module) {
  def generate(): HtmlFragment = {
    summaryTemplate.
      text(".size", detangled.cycleSize(module).toString).
      text(".depth", detangled.depth(module).toString).
      text(".complexity", detangled.complexity(module).toString)
  }
}
