package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

class StandaloneSummaryTemplateRulesImpl(detangled: Detangled) extends StandaloneSummaryTemplateRules {
  override def generate(summaryTemplate: HtmlElement, standalone: Standalone): HtmlElement = {
    summaryTemplate.
      attr(".standalone-summary", "id", HtmlUtil.htmlId(standalone)).
      text(".name", HtmlUtil.htmlName(standalone)).
      text(".depth", detangled.depth(standalone).toString).
      text(".complexity", detangled.complexity(standalone).toString).
      anchor(".composed-of", HtmlUtil.fileNameFor(standalone), "parts")
  }
}
