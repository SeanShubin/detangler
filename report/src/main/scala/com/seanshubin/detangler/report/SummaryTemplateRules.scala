package com.seanshubin.detangler.report

trait SummaryTemplateRules {
  def generate(template: HtmlElement): HtmlElement
}
