package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

import scala.collection.mutable.ArrayBuffer

class TableOfContentsTemplateRulesStub(generateResult: HtmlElement) extends TableOfContentsTemplateRules {
  val invocations = new ArrayBuffer[(HtmlElement, Standalone)]()

  override def generate(template: HtmlElement): HtmlElement = ???
}
