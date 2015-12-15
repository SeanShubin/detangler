package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait DependencyTemplateRules {
  def generate(detailTemplate: HtmlElement, single: Single, direction: DependencyDirection): HtmlElement
}
