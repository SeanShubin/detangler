package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Single

class SingleDetailTemplateRulesImpl extends SingleDetailTemplateRules {
  override def generate(detailTemplate: HtmlElement, single: Single): HtmlElement =
    HtmlElement.fragmentFromString("<p>SingleDetailTemplateRulesImpl not implemented</p>")
}
