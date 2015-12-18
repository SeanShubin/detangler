package com.seanshubin.detangler.report

import com.seanshubin.detangler.model._

class ModulesTemplateRulesImpl(singleTemplateRules: SingleTemplateRules,
                               cycleTemplateRules: CycleTemplateRules,
                               detangled: Detangled) extends ModulesTemplateRules {
  override def generate(modulesTemplate: HtmlElement, single: Single): HtmlElement = {
    val baseTemplate = modulesTemplate.remove(".single").remove(".cycle")
    val singleTemplate = modulesTemplate.select(".single")
    val cycleTemplate = modulesTemplate.select(".cycle")
    val children = detangled.childModules(single)
    def composeModuleFunction(module: Module): HtmlElement = composeModule(single, module, singleTemplate, cycleTemplate)
    val moduleElements = children.toSeq.seq.sortWith(ModuleOrdering.lessThan).map(composeModuleFunction)
    val result = baseTemplate.append(".append-module", moduleElements)
    result
  }

  private def composeModule(context: Single, module: Module, singleTemplate: HtmlElement, cycleTemplate: HtmlElement): HtmlElement = {
    module match {
      case cycle: Cycle => composeCycle(cycleTemplate, context, cycle)
      case single: Single => composeSingle(singleTemplate, context, single)
    }
  }

  private def composeCycle(cycleTemplate: HtmlElement, context:Single, cycle: Cycle): HtmlElement = {
    cycleTemplateRules.generate(cycleTemplate, context, cycle)
  }

  private def composeSingle(singleTemplate: HtmlElement, context: Single, single: Single): HtmlElement = {
    singleTemplateRules.generate(singleTemplate, context, single)
  }
}
