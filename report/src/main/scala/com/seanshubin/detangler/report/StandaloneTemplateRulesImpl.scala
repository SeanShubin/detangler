package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

class StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules: StandaloneSummaryTemplateRules,
                                  dependsOnTemplateRules: DependencyTemplateRules,
                                  dependedOnByTemplateRules: DependencyTemplateRules) extends StandaloneTemplateRules {
  override def generate(standaloneTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement = {
    val baseTemplate = standaloneTemplate.remove(".standalone-dependency")
    val summaryTemplate = standaloneTemplate.select(".standalone-summary")
    val dependencyTemplate = standaloneTemplate.select(".standalone-dependency")
    val summary = standaloneSummaryTemplateRules.generate(summaryTemplate, standalone)
    val dependsOn = dependsOnTemplateRules.generate(dependencyTemplate, context, standalone)
    val dependedOnBy = dependedOnByTemplateRules.generate(dependencyTemplate, context, standalone)
    val a = baseTemplate.replace(".standalone-summary", summary)
    val b = a.replaceOrRemove(".standalone-depends-on", dependsOn)
    val c = b.replaceOrRemove(".standalone-depended-on-by", dependedOnBy)
    c
  }
}
