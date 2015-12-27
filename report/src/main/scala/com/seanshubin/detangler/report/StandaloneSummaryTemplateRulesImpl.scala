package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

class StandaloneSummaryTemplateRulesImpl(detangled: Detangled) extends StandaloneSummaryTemplateRules {
  override def generate(summaryTemplate: HtmlElement, standalone: Standalone): HtmlElement = {
    summaryTemplate.
      attr(".standalone-summary", "id", HtmlRendering.htmlId(standalone)).
      text(".name", HtmlRendering.htmlName(standalone)).
      text(".depth", detangled.depth(standalone).toString).
      text(".breadth", detangled.breadth(standalone).toString).
      text(".transitive", detangled.transitive(standalone).toString).
      anchor(".composed-of", HtmlRendering.fileNameFor(standalone), "parts")
  }
}
