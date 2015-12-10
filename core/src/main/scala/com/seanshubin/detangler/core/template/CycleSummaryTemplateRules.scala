package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, UnitId}

class CycleSummaryTemplateRules(summaryTemplate: HtmlFragment, detangled: Detangled, unit: UnitId) {
  def generate(): HtmlFragment = {
    summaryTemplate.
      text(".size", detangled.cycleSize(unit).toString).
      text(".depth", detangled.depth(unit).toString).
      text(".complexity", detangled.complexity(unit).toString)
  }
}
