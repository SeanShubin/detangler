package com.seanshubin.detangler.report

import java.nio.charset.Charset

import com.seanshubin.detangler.model.Standalone

class GraphTemplateRulesStub(charset: Charset) extends GraphTemplateRules {
  override def generate(graphTemplate: HtmlElement, standalone: Standalone): HtmlElement = ???
}
