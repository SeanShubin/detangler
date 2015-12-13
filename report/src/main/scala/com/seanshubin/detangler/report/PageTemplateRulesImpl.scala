package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class PageTemplateRulesImpl(modulesTemplateRules: ModulesTemplateRules,
                            reasonsTemplateRules: ReasonsTemplateRules) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, single: Single): String = {
    val emptyPageTemplate = pageTemplate.remove(".modules").remove(".reasons")
    val modulesTemplate = pageTemplate.select(".modules")
    val reasonsTemplate = pageTemplate.select(".reasons")
    val modules = modulesTemplateRules.generate(modulesTemplate, single)
    val reasons = reasonsTemplateRules.generate(reasonsTemplate, single)
    val page = emptyPageTemplate.append("body", Seq(modules, reasons))
    page.text
  }
}
