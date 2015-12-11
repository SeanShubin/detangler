package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._
import com.seanshubin.detangler.core.template.FragmentSelectors._

class ModuleTemplateRules(moduleTemplate: HtmlFragment, detangled: Detangled, context: Module, module: Module) {
  private val emptyTemplate = moduleTemplate.remove(SelectorModuleSummary).remove(SelectorModuleDetail)
  private val summaryTemplate = moduleTemplate.one(SelectorModuleSummary)
  private val dependencyTemplate = moduleTemplate.one(SelectorModuleDetail)

  def generate(): HtmlFragment = {
    val summary = new ModuleSummaryTemplateRules(summaryTemplate, detangled, module).generate()
    val dependsOn = new DependencyTemplateRules(dependencyTemplate, detangled, context, module, ReasonDirection.TowardDependsOn).generate()
    val dependedOnBy = new DependencyTemplateRules(dependencyTemplate, detangled, context, module, ReasonDirection.TowardDependedOnBy).generate()
    emptyTemplate.appendChild(summary).appendChild(dependsOn).appendChild(dependedOnBy)
  }
}
