package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class SingleTemplateRulesImpl(singleSummaryTemplateRules: SingleSummaryTemplateRules,
                              singleDetailTemplateRules: SingleDetailTemplateRules) extends SingleTemplateRules {
  override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement = {
    val summaryTemplate = singleTemplate.select(".single-summary")
    val detailTemplate = singleTemplate.select(".single-detail")
    val summary = singleSummaryTemplateRules.generate(summaryTemplate, single)
    val detail = singleDetailTemplateRules.generate(detailTemplate, single)
    val result = singleTemplate.replace(".single-summary", summary).replace(".single-detail", detail)
    result
  }
}
