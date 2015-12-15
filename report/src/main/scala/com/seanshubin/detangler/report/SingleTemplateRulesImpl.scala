package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class SingleTemplateRulesImpl(singleSummaryTemplateRules: SingleSummaryTemplateRules,
                              dependsOnTemplateRules: DependencyTemplateRules,
                              dependedOnByTemplateRules: DependencyTemplateRules) extends SingleTemplateRules {
  override def generate(singleTemplate: HtmlElement, context: Single, single: Single): HtmlElement = {
    val summaryTemplate = singleTemplate.select(".single-summary")
    val dependsOnTemplate = singleTemplate.select(".single-depends-on")
    val dependedOnByTemplate = singleTemplate.select(".single-depended-on-by")
    val summary = singleSummaryTemplateRules.generate(summaryTemplate, single)
    val dependsOn = dependsOnTemplateRules.generate(dependsOnTemplate, context, single)
    val dependedOnBy = dependedOnByTemplateRules.generate(dependedOnByTemplate, context, single)
    val result = singleTemplate.
      replace(".single-summary", summary).
      replace(".single-depends-on", dependsOn).
      replace(".single-depended-on-by", dependedOnBy)
    result
  }
}
