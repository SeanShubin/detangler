package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class PageTemplateRulesImpl(modulesTemplateRules: ModulesTemplateRules,
                            reasonsTemplateRules: ReasonsTemplateRules) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, single: Single): HtmlElement = {
    val modulesTemplate = pageTemplate.select(".modules")
    val reasonsTemplate = pageTemplate.select(".reasons")
    val modules = modulesTemplateRules.generate(modulesTemplate, single)
    val reasons = reasonsTemplateRules.generate(reasonsTemplate, single, single)
    val page = pageTemplate.replace(".modules", modules).replace(".reasons", reasons)
    page
  }
}
