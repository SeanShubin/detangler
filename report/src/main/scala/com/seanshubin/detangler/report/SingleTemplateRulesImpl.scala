package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class SingleTemplateRulesImpl(singleSummaryTemplateRules: SingleSummaryTemplateRules,
                              dependsOnTemplateRules: DependencyTemplateRules,
                              dependedOnByTemplateRules: DependencyTemplateRules) extends SingleTemplateRules {
  override def generate(singleTemplate: HtmlElement, context: Single, single: Single): HtmlElement = {
    val baseTemplate = singleTemplate.remove(".single-dependency")
    val summaryTemplate = singleTemplate.select(".single-summary")
    val dependencyTemplate = singleTemplate.select(".single-dependency")
    val summary = singleSummaryTemplateRules.generate(summaryTemplate, single)
    val dependsOn = dependsOnTemplateRules.generate(dependencyTemplate, context, single)
    val dependedOnBy = dependedOnByTemplateRules.generate(dependencyTemplate, context, single)
    val result = baseTemplate.
      replace(".single-summary", summary).
      replace(".single-depends-on", dependsOn).
      replace(".single-depended-on-by", dependedOnBy)
    result
  }
}
