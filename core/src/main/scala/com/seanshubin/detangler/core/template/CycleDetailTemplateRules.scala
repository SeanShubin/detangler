package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.template.FragmentSelectors._
import com.seanshubin.detangler.core.{Detangled, HtmlFragment, HtmlUtil, Module}

class CycleDetailTemplateRules(detailTemplate: HtmlFragment, detangled: Detangled, pageModule: Module, module: Module) {
  private val emptyTemplate = detailTemplate.remove(SelectorCyclePart)
  private val partTemplate = detailTemplate.one(SelectorCyclePart)

  def generate(): HtmlFragment = {
    val children = detangled.composedOf(module).map(generateChild)
    emptyTemplate.appendAll(SelectorCycleParts, children)
  }

  private def generateChild(child: Module): HtmlFragment = {
    partTemplate.anchor(HtmlUtil.htmlLink(pageModule, child), HtmlUtil.htmlName(module))
  }
}
