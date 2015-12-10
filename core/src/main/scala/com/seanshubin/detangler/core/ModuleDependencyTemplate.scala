package com.seanshubin.detangler.core

class ModuleDependencyTemplate(template: HtmlFragment,
                               pageModule: Module,
                               parentModule: Module,
                               reasonDirection: ReasonDirection,
                               detangled: Detangled) {
  private val parentTemplate = template.remove(".dependency-row-inner")
  private val childTemplate = template.one(".dependency-row-inner")

  def generate(): HtmlFragment = {
    val childModules = reasonDirection.dependencies(detangled, pageModule, parentModule)
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
      anchor(".name", HtmlUtil.htmlLink(pageModule, childModule), HtmlUtil.htmlName(childModule)).
      text(".depth", detangled.depth(childModule).toString).
      text(".complexity", detangled.complexity(childModule).toString).
      anchor(".reason", reasonLink, reasonName)
  }
}
