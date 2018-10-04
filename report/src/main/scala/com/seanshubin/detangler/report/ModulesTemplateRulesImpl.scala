package com.seanshubin.detangler.report

import com.seanshubin.detangler.model._

class ModulesTemplateRulesImpl(standaloneTemplateRules: StandaloneTemplateRules,
                               cycleTemplateRules: CycleTemplateRules,
                               detangled: Detangled) extends ModulesTemplateRules {
  override def generate(modulesTemplate: HtmlElement, standalone: Standalone): HtmlElement = {
    val baseTemplate = modulesTemplate.remove(".standalone").remove(".cycle")
    val standaloneTemplate = modulesTemplate.select(".standalone")
    val cycleTemplate = modulesTemplate.select(".cycle")
    val children = detangled.childModules(standalone)

    def composeModuleFunction(module: Module): HtmlElement = composeModule(standalone, module, standaloneTemplate, cycleTemplate)

    val moduleElements = children.map(composeModuleFunction)
    val result = baseTemplate.append(".append-module", moduleElements)
    result
  }

  private def composeModule(context: Standalone,
                            module: Module,
                            standaloneTemplate: HtmlElement,
                            cycleTemplate: HtmlElement): HtmlElement = {
    module match {
      case cycle: Cycle => composeCycle(cycleTemplate, context, cycle)
      case standalone: Standalone => composeStandalone(standaloneTemplate, context, standalone)
    }
  }

  private def composeCycle(cycleTemplate: HtmlElement, context: Standalone, cycle: Cycle): HtmlElement = {
    cycleTemplateRules.generate(cycleTemplate, context, cycle)
  }

  private def composeStandalone(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement = {
    standaloneTemplateRules.generate(standaloneTemplate, context, standalone)
  }
}
