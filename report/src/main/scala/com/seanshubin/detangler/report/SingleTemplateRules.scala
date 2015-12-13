package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait SingleTemplateRules {
  def generate(singleTemplate: HtmlElement, single: Single): HtmlElement
}
