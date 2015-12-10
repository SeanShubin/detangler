package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._

class ModuleTemplateRules(moduleTemplate: HtmlFragment, detangled: Detangled, context: Module, module: Module) {
  private val emptyTemplate = moduleTemplate.remove(".summary").remove(".dependency")
  private val summaryTemplate = moduleTemplate.one(".summary")
  private val dependencyTemplate = moduleTemplate.one(".dependency")

  def generate(): HtmlFragment = {
    val summary = new ModuleSummaryTemplateRules(summaryTemplate, detangled, module).generate()
    val dependsOn = new DependencyTemplateRules(dependencyTemplate, detangled, context, module, ReasonDirection.TowardDependsOn).generate()
    val dependedOnBy = new DependencyTemplateRules(dependencyTemplate, detangled, context, module, ReasonDirection.TowardDependsOn).generate()
    emptyTemplate.appendChild(summary).appendChild(dependsOn).appendChild(dependedOnBy)
  }
}
