package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Module, ModuleOrdering, Single}

class ModulesTemplateRulesImpl(singleTemplateRules: SingleTemplateRules,
                               cycleTemplateRules: CycleTemplateRules,
                               detangled: Detangled) extends ModulesTemplateRules {
  override def generate(modulesTemplate: HtmlElement, single: Single): HtmlElement = {
    val baseTemplate = modulesTemplate.remove(".single").remove(".cycle")
    val singleTemplate = modulesTemplate.select(".single")
    val cycleTemplate = modulesTemplate.select(".cycle")
    val children = detangled.children(single)
    val moduleElements = children.toSeq.seq.sortWith(ModuleOrdering.lessThan).map(composeModule)
    val result = baseTemplate.append(".module-append-here", moduleElements)
    result
  }

  private def composeModule(module: Module): HtmlElement = {
    ???
  }
}
