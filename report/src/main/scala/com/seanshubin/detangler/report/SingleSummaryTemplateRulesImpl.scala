package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Single}

class SingleSummaryTemplateRulesImpl(detangled: Detangled) extends SingleSummaryTemplateRules {
  override def generate(summaryTemplate: HtmlElement, single: Single): HtmlElement = {
    summaryTemplate.
      attr(".single-summary", "id", HtmlUtil.htmlId(single)).
      text(".name", HtmlUtil.htmlName(single)).
      text(".depth", detangled.depth(single).toString).
      text(".complexity", detangled.complexity(single).toString).
      anchor(".composed-of", HtmlUtil.fileNameFor(single), "parts")
  }
}
