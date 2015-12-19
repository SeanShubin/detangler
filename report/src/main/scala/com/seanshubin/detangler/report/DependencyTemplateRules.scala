package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait DependencyTemplateRules {
  def generate(detailTemplate: HtmlElement, context: Standalone, standalone: Standalone): QuantityAndElement
}
