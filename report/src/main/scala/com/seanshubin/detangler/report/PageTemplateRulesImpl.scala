package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

class PageTemplateRulesImpl(modulesTemplateRules: ModulesTemplateRules,
                            reasonsTemplateRules: ReasonsTemplateRules) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, standalone: Standalone): HtmlElement = {
    val modulesTemplate = pageTemplate.select(".modules")
    val reasonsTemplate = pageTemplate.select(".reasons")
    val modules = modulesTemplateRules.generate(modulesTemplate, standalone)
    val reasons = reasonsTemplateRules.generate(reasonsTemplate, standalone, standalone)
    val page = pageTemplate.replace(".modules", modules).replace(".reasons", reasons)
    page
  }
}
