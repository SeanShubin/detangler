package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait ReasonsTemplateRules {
  def generate(reasonsTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement
}
