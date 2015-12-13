package com.seanshubin.detangler.report

import com.seanshubin.detangler.modle.Single

trait ModulesTemplateRules {
  def generate(modulesTemplate: HtmlElement, single: Single): HtmlElement
}
