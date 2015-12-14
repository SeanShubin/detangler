package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait PageTemplateRules {
  def generate(pageTemplate: HtmlElement, single: Single): HtmlElement
}
