package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

class PageTemplateRulesImpl(modulesTemplateRules: ModulesTemplateRules,
                            reasonsTemplateRules: ReasonsTemplateRules) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, context: Standalone, isLeafPage: Boolean): HtmlElement = {
    val modulesTemplate = pageTemplate.select(".modules")
    val reasonsTemplate = pageTemplate.select(".reasons")
    val modules = modulesTemplateRules.generate(modulesTemplate, context)
    val reasons = reasonsTemplateRules.generate(reasonsTemplate, context, context)
    val parentLink = HtmlRender.navigateHigherLink(context)
    val parentName = HtmlRender.navigateHigherLinkName(context)
    val graphLink = HtmlRender.graphLink(context)
    val graphName = HtmlRender.graphLinkName(context)
    val a = pageTemplate.replace(".modules", modules).replace(".reasons", reasons)
    val b = a.anchor(".parent", parentLink, parentName)
    val c = b.anchor(".graph", graphLink, graphName)
    val d = if (isLeafPage) c.removeAll(".delete-if-leaf") else c
    d
  }
}
