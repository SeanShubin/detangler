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
    val a = baseTemplate.replace(".single-summary", summary)
    val b = replaceIfPositiveQuantity(".single-depends-on", a, dependsOn)
    val c = replaceIfPositiveQuantity(".single-depended-on-by", b, dependedOnBy)
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
