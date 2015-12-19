package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

class PageTemplateRulesImpl(modulesTemplateRules: ModulesTemplateRules,
                            reasonsTemplateRules: ReasonsTemplateRules,
                            parentLinkFunction: Standalone => String,
                            parentTextFunction: Standalone => String) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, context: Standalone, isLeafPage: Boolean): HtmlElement = {
    val modulesTemplate = pageTemplate.select(".modules")
    val reasonsTemplate = pageTemplate.select(".reasons")
    val modules = modulesTemplateRules.generate(modulesTemplate, context)
    val reasons = reasonsTemplateRules.generate(reasonsTemplate, context, context)
    val a = pageTemplate.replace(".modules", modules).replace(".reasons", reasons)
    val b = if (context.isRoot) a.remove(".parent") else a.anchor(".parent", parentLinkFunction(context.parent), parentTextFunction(context))
    val c = if (isLeafPage) b.removeAll(".delete-if-leaf") else b
    c
  }
}
