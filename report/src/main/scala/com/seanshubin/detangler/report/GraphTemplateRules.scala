package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait GraphTemplateRules {
  def generate(graphTemplate: HtmlElement, standalone: Standalone, graphRenderResult: GraphRenderResult): HtmlElement
}
