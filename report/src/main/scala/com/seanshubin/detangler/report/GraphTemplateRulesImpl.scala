package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

class GraphTemplateRulesImpl() extends GraphTemplateRules {
  override def generate(graphTemplate: HtmlElement, context: Standalone): HtmlElement = {
    val graphFile = HtmlRendering.graphTargetFile(context)
    val reportFile = HtmlRendering.reportFile(context)

    graphTemplate.
      anchor(".parent", reportFile, reportFile).
      attr(".graph", "src", graphFile).attr(".graph", "alt", "graphFile")
  }
}
