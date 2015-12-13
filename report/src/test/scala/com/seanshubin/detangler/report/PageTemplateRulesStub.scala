package com.seanshubin.detangler.report

import java.nio.charset.Charset

import com.seanshubin.detangler.modle.Single

class PageTemplateRulesStub(contentMap: Map[Single, String], charset: Charset) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, single: Single): String = {
    contentMap(single)
  }
}
