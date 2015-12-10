package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class PageTemplateRules(pageTemplate: HtmlFragment, detangled: Detangled, module: Module) {
  private val emptyTemplate = pageTemplate.remove(".modules").remove(".reasons")
  private val modulesTemplate = pageTemplate.one(".modules")
  private val reasonsTemplate = pageTemplate.one(".reasons")

  def generate(): HtmlFragment = {
    val modules = new ModulesTemplateRules(modulesTemplate, detangled, module).generate()
    val reasons = detangled.reasonsFor(module)
    val reasonsFragment = new ReasonsTemplateRules(reasonsTemplate, detangled, module, reasons).generate()
    emptyTemplate.appendChild(modules).appendChild(reasonsFragment)
  }
}
