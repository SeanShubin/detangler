package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Module, Standalone}

class DependencyTemplateRulesImpl(detangled: Detangled, dependencyDirection: DependencyDirection) extends DependencyTemplateRules {
  override def generate(dependencyTemplate: HtmlElement,
                        context: Standalone,
                        module: Module): Option[HtmlElement] = {
    val baseTemplate = dependencyTemplate.remove(".standalone-dependency-row")
    val dependencyRowTemplate = dependencyTemplate.select(".standalone-dependency-row")
    val childModules = dependencyDirection.dependenciesFor(detangled, module)
    if (childModules.isEmpty) {
      None
    } else {
      def generateRowFunction(x: Standalone) = generateRow(dependencyRowTemplate, context, module, x)
      val rows = childModules.map(generateRowFunction)
      val result = baseTemplate.
        text(".caption", s"${dependencyDirection.caption} (${childModules.size})").
        append(".standalone-append-dependency-row", rows)
      Some(result)
    }
  }

  private def generateRow(dependencyRowTemplate: HtmlElement, context: Standalone, parentModule: Module, child: Standalone): HtmlElement = {
    val partCount = detangled.childModules(child).size
    val partString = if (partCount == 1) {
      "1 part"
    } else {
      s"$partCount parts"
    }
    val reportLink = HtmlRender.reportPageLink(child)
    val childLink = HtmlRender.moduleLink(context, child)
    val childName = HtmlRender.moduleLinkName(child)
    parentModule match {
      case parent: Standalone =>
        val reasonName = dependencyDirection.name(parent, child)
        val reasonLink = dependencyDirection.link(parent, child)
        cycleLink(dependencyRowTemplate, child).
          anchor(".name", childLink, childName).
          text(".depth", detangled.depth(child).toString).
          text(".breadth", detangled.breadth(child).toString).
          text(".transitive", detangled.transitive(child).toString).
          anchor(".reason", reasonLink, reasonName).
          anchor(".composed-of", reportLink, partString)
      case parent: Cycle =>
        cycleLink(dependencyRowTemplate, child).
          anchor(".name", childLink, childName).
          text(".depth", detangled.depth(child).toString).
          text(".breadth", detangled.breadth(child).toString).
          text(".transitive", detangled.transitive(child).toString).
          anchor(".composed-of", reportLink, partString)
    }
  }

  private def cycleLink(template: HtmlElement, standalone: Standalone): HtmlElement = {
    detangled.partOfCycle(standalone) match {
      case Some(cycle) =>
        template.
          attr(".cycle-link", "href", HtmlRender.relativeModuleLink(cycle))
      case None =>
        template.remove(".cycle-link")
    }
  }
}
