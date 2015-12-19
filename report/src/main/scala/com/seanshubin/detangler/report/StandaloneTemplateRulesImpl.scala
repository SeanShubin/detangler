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
    val b = replaceIfPositiveQuantity(".standalone-depends-on", a, dependsOn)
    val c = replaceIfPositiveQuantity(".standalone-depended-on-by", b, dependedOnBy)
    c
  }

  private def replaceIfPositiveQuantity(cssQuery: String, base: HtmlElement, quantityAndElement: QuantityAndElement): HtmlElement = {
    val QuantityAndElement(quantity, element) = quantityAndElement
    if (quantity == 0) {
      base.remove(cssQuery)
    } else {
      base.replace(cssQuery, element)
    }
  }
}
