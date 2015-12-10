package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, Module}

class ModulesTemplateRules(modulesTemplate: HtmlFragment, detangled: Detangled, parentModule: Module) {
  private val emptyTemplate = modulesTemplate.remove(".module").remove(".cycle")
  private val moduleTemplate = modulesTemplate.one(".module")
  private val cycleTemplate = modulesTemplate.one(".cycle")

  def generate(): HtmlFragment = {
    val modulesAndCycles = detangled.composedOf(parentModule).map(generateModuleOrCycle)
    emptyTemplate.appendAll(".modules", modulesAndCycles)
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
