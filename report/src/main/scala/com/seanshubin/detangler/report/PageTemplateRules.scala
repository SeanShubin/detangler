package com.seanshubin.detangler.report

import com.seanshubin.detangler.modle.Single

trait PageTemplateRules {
  def generate(pageTemplate: HtmlElement, single: Single): String
}
