package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait SingleSummaryTemplateRules {
  def generate(summaryTemplate: HtmlElement, single: Single): HtmlElement
}
