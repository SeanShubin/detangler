package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

class StandaloneSummaryTemplateRulesImpl(detangled: Detangled) extends StandaloneSummaryTemplateRules {
  override def generate(summaryTemplate: HtmlElement, standalone: Standalone): HtmlElement = {
    val partCount = detangled.childModules(standalone).size
    val partString = if (partCount == 1) {
      "1 part"
    } else {
      s"$partCount parts"
    }
    val reportLink = HtmlRender.reportPageLink(standalone)
    cycleLink(summaryTemplate, standalone).
      attr(".standalone-summary", "id", HtmlRender.id(standalone)).
      text(".name", HtmlRender.moduleLinkName(standalone)).
      text(".depth", detangled.depth(standalone).toString).
      text(".breadth", detangled.breadth(standalone).toString).
      text(".transitive", detangled.transitive(standalone).toString).
      anchor(".composed-of", reportLink, partString)
  }

  private def cycleLink(template: HtmlElement, standalone: Standalone): HtmlElement = {
    val element = detangled.partOfCycle(standalone) match {
      case Some(cycle) =>
        template.
          attr(".cycle-link", "href", HtmlRender.relativeModuleLink(cycle))
      case None =>
        template.remove(".cycle-link")
    }
    element
  }
}
