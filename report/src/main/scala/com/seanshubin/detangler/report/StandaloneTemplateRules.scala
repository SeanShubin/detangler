package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait StandaloneTemplateRules {
  def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement
}
