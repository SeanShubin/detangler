package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

import scala.collection.mutable.ArrayBuffer

class GraphTemplateRulesStub(generateResult: HtmlElement) extends GraphTemplateRules {
  val invocations = new ArrayBuffer[(HtmlElement, Standalone)]()

  override def generate(graphTemplate: HtmlElement, standalone: Standalone): HtmlElement = {
    generateResult
  }
}
