package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.template.FragmentSelectors._
import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module, Reason}

class PageTemplateRules(pageTemplate: HtmlFragment, detangled: Detangled, module: Module) {
  private val emptyTemplate = pageTemplate.remove(SelectorModules).remove(SelectorReason)
  private val modulesTemplate = pageTemplate.one(SelectorModules)
  private val reasonsTemplate = pageTemplate.one(SelectorReasons)

  def generate(): HtmlFragment = {
    val modules = new ModulesTemplateRules(modulesTemplate, detangled, module).generate()
    val reasons = detangled.reasonsFor(module)
    val modulesAppended = emptyTemplate.appendChild(modules)
    val result = if (Reason.depth(reasons) > 1) {
      val reasonsFragment = new ReasonsTemplateRules(reasonsTemplate, detangled, module, reasons).generate()
      modulesAppended.appendChild(reasonsFragment)
    } else {
      modulesAppended
    }
    result
  }
}
