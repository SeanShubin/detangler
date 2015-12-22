package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

class DependencyTemplateRulesImpl(detangled: Detangled, dependencyDirection: DependencyDirection) extends DependencyTemplateRules {
  override def generate(dependencyTemplate: HtmlElement,
                        context: Standalone,
                        standalone: Standalone): QuantityAndElement = {
    val baseTemplate = dependencyTemplate.remove(".standalone-dependency-row")
    val dependencyRowTemplate = dependencyTemplate.select(".standalone-dependency-row")
    val childModules = dependencyDirection.dependenciesFor(detangled, standalone)
    def generateRowFunction(x: Standalone) = generateRow(dependencyRowTemplate, context, standalone, x)
    val rows = childModules.map(generateRowFunction)
    val result = baseTemplate.
      text(".caption", s"${dependencyDirection.caption} (${childModules.size})").
      append(".standalone-append-dependency-row", rows)
    QuantityAndElement(childModules.size, result)
  }

  private def generateRow(dependencyRowTemplate: HtmlElement, context: Standalone, parent: Standalone, child: Standalone): HtmlElement = {
    val reasonName = dependencyDirection.name(parent, child)
    val reasonLink = dependencyDirection.link(parent, child)

    dependencyRowTemplate.
      anchor(".name", HtmlRendering.htmlLink(context, child), HtmlRendering.htmlName(child)).
      text(".depth", detangled.depth(child).toString).
      text(".complexity", detangled.transitive(child).toString).
      anchor(".reason", reasonLink, reasonName)
  }
}
