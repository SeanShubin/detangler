package com.seanshubin.detangler.report

import java.nio.charset.Charset

import com.seanshubin.detangler.model.Standalone

import scala.collection.mutable.ArrayBuffer

class TableOfContentsTemplateRulesStub(result: HtmlElement) extends TableOfContentsTemplateRules {
  val invocations = new ArrayBuffer[HtmlElement]()

  override def generate(template: HtmlElement): HtmlElement = {
    invocations.append(template)
    result
  }
}
