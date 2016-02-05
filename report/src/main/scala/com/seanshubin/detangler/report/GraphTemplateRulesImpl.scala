package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

class GraphTemplateRulesImpl() extends GraphTemplateRules {
  override def generate(graphTemplate: HtmlElement, context: Standalone, graphRenderResult: GraphRenderResult): HtmlElement = {
    val graphFile = HtmlRender.graphTargetLink(context)
    val reportFile = HtmlRender.reportPageLink(context)

    graphRenderResult match {
      case GraphRenderSuccess =>
        graphTemplate.
          anchor(".parent", reportFile, reportFile).
          attr(".graph", "src", graphFile).attr(".graph", "alt", "graphFile").
          remove(".error")
      case GraphRenderFailure(command) =>
        graphTemplate.
          anchor(".parent", reportFile, reportFile).
          text(".command", command.mkString(" ")).
          remove(".graph")
    }
  }
}
