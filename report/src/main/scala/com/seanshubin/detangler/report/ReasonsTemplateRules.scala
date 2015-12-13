package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait ReasonsTemplateRules {
  def generate(reasonsTemplate: HtmlElement, single: Single): HtmlElement
}
