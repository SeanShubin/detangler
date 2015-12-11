package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.template.FragmentSelectors._
import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class ModulesTemplateRules(modulesTemplate: HtmlFragment, detangled: Detangled, parentModule: Module) {
  private val emptyTemplate = modulesTemplate.remove(SelectorModule).remove(SelectorCycle)
  private val moduleTemplate = modulesTemplate.one(SelectorModule)
  private val cycleTemplate = modulesTemplate.one(SelectorCycle)

  def generate(): HtmlFragment = {
    val modulesAndCycles = detangled.children(parentModule).map(generateModuleOrCycle)
    val result = emptyTemplate.appendAll(SelectorModules, modulesAndCycles)
    result
  }

  private def generateModuleOrCycle(module: Module): HtmlFragment = {
    val result = if (module.isCycle) {
      val cycleTemplateRules = new CycleTemplateRules(cycleTemplate, detangled, parentModule, module)
      cycleTemplateRules.generate()
    } else {
      val moduleTemplateRules = new ModuleTemplateRules(moduleTemplate, detangled, parentModule, module)
      moduleTemplateRules.generate()
    }
    result
  }
}
