package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, HtmlUtil, UnitId}

class UnitSummaryTemplateRules(summaryTemplate: HtmlFragment, detangled: Detangled, unit: UnitId) {
  def generate(): HtmlFragment = {
    summaryTemplate.
      attr(".summary", "id", HtmlUtil.htmlId(unit)).
      text(".name", HtmlUtil.htmlName(unit)).
      text(".depth", detangled.depth(unit).toString).
      text(".complexity", detangled.complexity(unit).toString).
      anchor(".composed-of", HtmlUtil.fileNameFor(unit), "parts")
  }
}
