package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Standalone}

class DependencyTemplateRulesImpl(detangled: Detangled, dependencyDirection: DependencyDirection) extends DependencyTemplateRules {
  override def generate(dependencyTemplate: HtmlElement,
                        context: Standalone,
                        module: Module): QuantityAndElement = {
    val baseTemplate = dependencyTemplate.remove(".standalone-dependency-row")
    val dependencyRowTemplate = dependencyTemplate.select(".standalone-dependency-row")
    val childModules = dependencyDirection.dependenciesFor(detangled, module)
    def generateRowFunction(x: Standalone) = generateRow(dependencyRowTemplate, context, module, x)
    val rows = childModules.map(generateRowFunction)
    val result = baseTemplate.
      text(".caption", s"${dependencyDirection.caption} (${childModules.size})").
      append(".standalone-append-dependency-row", rows)
    QuantityAndElement(childModules.size, result)
  }

  private def generateRow(dependencyRowTemplate: HtmlElement, context: Standalone, parentModule: Module, child: Standalone): HtmlElement = {
    parentModule match {
      case parent: Standalone =>
        val reasonName = dependencyDirection.name(parent, child)
        val reasonLink = dependencyDirection.link(parent, child)
        cycleLink(dependencyRowTemplate, child).
          anchor(".name", HtmlRendering.htmlLink(context, child), HtmlRendering.htmlName(child)).
          text(".depth", detangled.depth(child).toString).
          text(".breadth", detangled.breadth(child).toString).
          text(".transitive", detangled.transitive(child).toString).
          anchor(".reason", reasonLink, reasonName)
      case parent: Cycle =>
        cycleLink(dependencyRowTemplate, child).
          anchor(".name", HtmlRendering.htmlLink(context, child), HtmlRendering.htmlName(child)).
          text(".depth", detangled.depth(child).toString).
          text(".breadth", detangled.breadth(child).toString).
          text(".transitive", detangled.transitive(child).toString)
    }
  }

  private def cycleLink(template: HtmlElement, standalone: Standalone): HtmlElement = {
    detangled.partOfCycle(standalone) match {
      case Some(cycle) =>
        template.
          attr(".cycle-link", "href", HtmlRendering.cycleLink(cycle))
      case None =>
        template.remove(".cycle-link")
    }
  }
}
