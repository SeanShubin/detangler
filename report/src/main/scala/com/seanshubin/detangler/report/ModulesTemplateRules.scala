package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

trait ModulesTemplateRules {
  def generate(modulesTemplate: HtmlElement, single: Single): HtmlElement
}
