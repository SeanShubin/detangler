package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait ModulesTemplateRules {
  def generate(modulesTemplate: HtmlElement, standalone: Standalone): HtmlElement
}
