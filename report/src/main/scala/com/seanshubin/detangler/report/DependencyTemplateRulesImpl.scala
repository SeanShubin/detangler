package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Single}

class DependencyTemplateRulesImpl(detangled: Detangled, dependencyDirection: DependencyDirection) extends DependencyTemplateRules {
  override def generate(dependencyTemplate: HtmlElement,
                        context: Single,
                        single: Single): HtmlElement = {
    val baseTemplate = dependencyTemplate.remove(".dependency-row")
    val dependencyRowTemplate = dependencyTemplate.select(".dependency-row")
    val childModules = dependencyDirection.dependenciesFor(detangled, context, single)
    def generateRowFunction(x: Single) = generateRow(dependencyRowTemplate, context, single, x)
    val rows = childModules.map(generateRowFunction)
    val result = baseTemplate.
      text(".caption", s"${dependencyDirection.caption} (${childModules.size})").
      append(".append-dependency-row", rows)
    result
  }

  private def generateRow(dependencyRowTemplate: HtmlElement, context: Single, parent: Single, child: Single): HtmlElement = {
    val reasonName = dependencyDirection.name(parent, child)
    val reasonLink = dependencyDirection.link(parent, child)

    dependencyRowTemplate.
      anchor(".name", HtmlUtil.htmlLink(context, child), HtmlUtil.htmlName(child)).
      text(".depth", detangled.depth(child).toString).
      text(".complexity", detangled.complexity(child).toString).
      anchor(".reason", reasonLink, reasonName)
  }
}
