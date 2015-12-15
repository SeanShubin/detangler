package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait SingleTemplateRules {
  def generate(singleTemplate: HtmlElement, context: Single, single: Single): HtmlElement
}
