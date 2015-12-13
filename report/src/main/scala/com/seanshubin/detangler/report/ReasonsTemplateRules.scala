package com.seanshubin.detangler.report

import com.seanshubin.detangler.modle.Single

trait ReasonsTemplateRules {
  def generate(reasonsTemplate: HtmlElement, single: Single): HtmlElement
}
