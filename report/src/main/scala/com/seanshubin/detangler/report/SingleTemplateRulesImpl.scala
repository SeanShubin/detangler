package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class SingleTemplateRulesImpl(singleSummaryTemplateRules: SingleSummaryTemplateRules,
                              singleDetailTemplateRules: SingleDetailTemplateRules) extends SingleTemplateRules {
  override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement = {
    val summaryTemplate = singleTemplate.select(".single-summary")
    val dependsOnTemplate = singleTemplate.select(".single-depends-on")
    val dependedOnByTemplate = singleTemplate.select(".single-depended-on-by")
    val summary = singleSummaryTemplateRules.generate(summaryTemplate, single)
    val dependsOn = singleDetailTemplateRules.generate(dependsOnTemplate, single, DependencyDirection.TowardDependsOn)
    val dependedOnBy = singleDetailTemplateRules.generate(dependedOnByTemplate, single, DependencyDirection.TowardDependedOnBy)
    val result = singleTemplate.
      replace(".single-summary", summary).
      replace(".single-depends-on", dependsOn).
      replace(".single-depended-on-by", dependedOnBy)
    result
  }
}
