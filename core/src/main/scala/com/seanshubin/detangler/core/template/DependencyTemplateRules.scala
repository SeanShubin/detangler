package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._

class DependencyTemplateRules(dependencyTemplate: HtmlFragment,
                              detangled: Detangled,
                              context: Module,
                              parentModule: Module,
                              reasonDirection: ReasonDirection) {
  private val parentTemplate = dependencyTemplate.remove(".dependency-header")
  private val childTemplate = dependencyTemplate.one(".dependency-detail")

  def generate(): HtmlFragment = {
    val childModules = reasonDirection.dependencies(detangled, context, parentModule)
    if (childModules.isEmpty) {
      HtmlFragment.Empty
    } else {
      val rows = childModules.map(generateRow)
      val result = parentTemplate.
        text(".caption", s"${reasonDirection.caption} (${childModules.size})").
        appendAll(".dependency-row-outer", rows)
      result
    }
  }

  private def generateRow(childModule: Module): HtmlFragment = {
    val reasonName = reasonDirection.name(parentModule, childModule)
    val reasonLink = reasonDirection.link(parentModule, childModule)
    childTemplate.
      anchor(".name", HtmlUtil.htmlLink(context, childModule), HtmlUtil.htmlName(childModule)).
      text(".depth", detangled.depth(childModule).toString).
      text(".complexity", detangled.complexity(childModule).toString).
      anchor(".reason", reasonLink, reasonName)
  }
}
