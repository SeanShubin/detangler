package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}

class SummaryTemplateRulesImpl(detangled: Detangled) extends SummaryTemplateRules {
  override def generate(template: HtmlElement, entryPoints: Seq[Standalone], cycles: Seq[Cycle]): HtmlElement = {
    val emptyEntryPointsFragment = template.select(".entry-points").remove(".entry-point")
    val entryPointTemplate = template.select(".entry-point")
    val entryPointsFragment = emptyEntryPointsFragment.append(".append-entry-point", generateEntryPoints(entryPointTemplate, entryPoints))
    template.replace(".entry-points", entryPointsFragment)
  }

  private def generateEntryPoints(template: HtmlElement, entryPoints: Seq[Standalone]): Iterable[HtmlElement] = {
    def generateEntryPointFunction(standalone: Standalone): HtmlElement = {
      generateEntryPoint(template, standalone)
    }
    entryPoints.map(generateEntryPointFunction)
  }

  private def generateEntryPoint(template: HtmlElement, standalone: Standalone): HtmlElement = {
    template.
      anchor(".name", HtmlRendering.outerHtmlLinkFor(standalone), HtmlRendering.htmlName(standalone)).
      text(".depth", detangled.depth(standalone).toString).
      text(".breadth", detangled.depth(standalone).toString).
      text(".transitive", detangled.depth(standalone).toString)
  }
}
