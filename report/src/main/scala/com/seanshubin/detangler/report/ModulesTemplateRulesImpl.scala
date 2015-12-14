package com.seanshubin.detangler.report

import com.seanshubin.detangler.model._

class ModulesTemplateRulesImpl(singleTemplateRules: SingleTemplateRules,
                               cycleTemplateRules: CycleTemplateRules,
                               detangled: Detangled) extends ModulesTemplateRules {
  override def generate(modulesTemplate: HtmlElement, single: Single): HtmlElement = {
    val baseTemplate = modulesTemplate.remove(".single").remove(".cycle")
    val singleTemplate = modulesTemplate.select(".single")
    val cycleTemplate = modulesTemplate.select(".cycle")
    val children = detangled.children(single)
    def composeModuleFunction(module: Module): HtmlElement = composeModule(module, singleTemplate, cycleTemplate)
    val moduleElements = children.toSeq.seq.sortWith(ModuleOrdering.lessThan).map(composeModuleFunction)
    val result = baseTemplate.append(".append-module", moduleElements)
    result
  }

  private def composeModule(module: Module, singleTemplate: HtmlElement, cycleTemplate: HtmlElement): HtmlElement = {
    module match {
      case cycle: Cycle => composeCycle(cycleTemplate, cycle)
      case single: Single => composeSingle(singleTemplate, single)
    }
  }

  private def composeCycle(cycleTemplate: HtmlElement, cycle: Cycle): HtmlElement = {
    cycleTemplateRules.generate(cycleTemplate, cycle)
  }

  private def composeSingle(singleTemplate: HtmlElement, single: Single): HtmlElement = {
    singleTemplateRules.generate(singleTemplate, single)
  }
}
