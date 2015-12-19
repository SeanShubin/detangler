package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait StandaloneSummaryTemplateRules {
  def generate(summaryTemplate: HtmlElement, standalone: Standalone): HtmlElement
}
