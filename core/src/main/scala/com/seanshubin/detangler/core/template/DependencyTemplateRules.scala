package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._
import com.seanshubin.detangler.core.template.FragmentSelectors._

class DependencyTemplateRules(detailTemplate: HtmlFragment,
                              detangled: Detangled,
                              context: Module,
                              parentModule: Module,
                              reasonDirection: ReasonDirection) {
  private val wrapperTemplate = detailTemplate.remove(SelectorModuleDependency)
  private val childTemplate = detailTemplate.one(SelectorModuleDependency)

  def generate(): HtmlFragment = {
    val childModules = reasonDirection.dependencies(detangled, context, parentModule)
    if (childModules.isEmpty) {
      HtmlFragment.Empty
    } else {
      val rows = childModules.map(generateRow)
      val result = wrapperTemplate.
        text(".caption", s"${reasonDirection.caption} (${childModules.size})").
        appendAll(SelectorModuleDependencies, rows)
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
