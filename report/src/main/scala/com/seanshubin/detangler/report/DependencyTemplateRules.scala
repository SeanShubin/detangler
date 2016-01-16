package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Module, Standalone}

trait DependencyTemplateRules {
  def generate(detailTemplate: HtmlElement, context: Standalone, module: Module): Option[HtmlElement]
}
