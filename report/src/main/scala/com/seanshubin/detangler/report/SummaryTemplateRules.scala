package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Detangled

trait SummaryTemplateRules {
  def generate(template:HtmlElement, detangled:Detangled):HtmlElement
}
