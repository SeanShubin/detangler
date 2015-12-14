package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class SingleTemplateRulesImpl(singleSummaryTemplateRules: SingleSummaryTemplateRules,
                              singleDetailTemplateRules: SingleDetailTemplateRules,
                              context: Single) extends SingleTemplateRules {
  override def generate(singleTemplate: HtmlElement, single: Single): HtmlElement = {
    val summaryTemplate = singleTemplate.select(".summary")
    val detailTemplate = singleTemplate.select(".detail")
    val summary = singleSummaryTemplateRules.generate(summaryTemplate, single)
    val detail = singleDetailTemplateRules.generate(detailTemplate, single)
    val result = singleTemplate.replace(".summary", summary).replace(".detail", detail)
    result
  }
}
