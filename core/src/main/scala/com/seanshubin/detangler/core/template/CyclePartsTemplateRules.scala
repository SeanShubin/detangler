package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.template.FragmentSelectors._
import com.seanshubin.detangler.core.{Detangled, HtmlFragment, HtmlUtil, Module}

class CyclePartsTemplateRules(partsTemplate: HtmlFragment, detangled: Detangled, pageModule: Module, module: Module) {
  private val parentTemplate = partsTemplate.remove(SelectorCyclePart)
  private val childTemplate = partsTemplate.one(SelectorCyclePart)

  def generate(): HtmlFragment = {
    val children = detangled.composedOf(module).map(generateChild)
    parentTemplate.appendAll(children)
  }

  private def generateChild(child: Module): HtmlFragment = {
    childTemplate.anchor(HtmlUtil.htmlLink(pageModule, child), HtmlUtil.htmlName(module))
  }
}
