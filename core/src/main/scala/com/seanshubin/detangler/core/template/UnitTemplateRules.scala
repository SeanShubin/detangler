package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, ReasonDirection, UnitId}

class UnitTemplateRules(unitTemplate: HtmlFragment, detangled: Detangled, context: UnitId, unit: UnitId) {
  private val emptyTemplate = unitTemplate.remove(".summary").remove(".dependency")
  private val summaryTemplate = unitTemplate.one(".summary")
  private val dependencyTemplate = unitTemplate.one(".dependency")

  def generate(): HtmlFragment = {
    val summary = new UnitSummaryTemplateRules(summaryTemplate, detangled, unit).generate()
    val dependsOn = new DependencyTemplateRules(dependencyTemplate, detangled, context, unit, ReasonDirection.TowardDependsOn).generate()
    val dependedOnBy = new DependencyTemplateRules(dependencyTemplate, detangled, context, unit, ReasonDirection.TowardDependsOn).generate()
    emptyTemplate.appendChild(summary).appendChild(dependsOn).appendChild(dependedOnBy)
  }
}
