package com.seanshubin.detangler.report

import java.nio.charset.Charset

import com.seanshubin.detangler.model.Standalone

class PageTemplateRulesStub(contentMap: Map[Standalone, String], charset: Charset) extends PageTemplateRules {
  override def generate(pageTemplate: HtmlElement, standalone: Standalone, levelsDeep: Boolean): HtmlElement = {
    val elementText = contentMap(standalone)
    val element = HtmlElement.fragmentFromString(elementText)
    element
  }
}
