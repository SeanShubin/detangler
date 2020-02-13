package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

import scala.annotation.tailrec

class TableOfContentsTemplateRulesImpl(detangled: Detangled) extends TableOfContentsTemplateRules {
  override def generate(template: HtmlElement): HtmlElement = {
    val empty = template.remove(".table-of-contents-row")
    val rowTemplate = template.select(".table-of-contents-row").remove(".table-of-contents-cell")
    val cellTemplate = template.select(".table-of-contents-cell")

    def appendChild(appendToMe: HtmlElement, standalone: Standalone): HtmlElement = {
      val cells = standaloneToCells(cellTemplate, standalone)
      val row = rowTemplate.append(".table-of-contents-row", cells)
      appendToMe.append("#table-of-contents-body", row)
    }

    val children = detangled.allStandalone()
    children.foldLeft(empty)(appendChild)
  }


  private def standaloneToCells(cellTemplate:HtmlElement, standalone: Standalone): Seq[HtmlElement] = {
    standaloneToCellsRecursive(Nil, cellTemplate, standalone)
  }

  @tailrec
  private def standaloneToCellsRecursive(soFar: List[HtmlElement], cellTemplate:HtmlElement, standalone: Standalone): Seq[HtmlElement] = {
    if (standalone.isRoot) {
      soFar
    } else {
      val text = HtmlRender.reportPageLinkName(standalone)
      val href = HtmlRender.absoluteModuleLink(standalone)
      val cell = cellTemplate.anchor(".link", href, text)
      standaloneToCellsRecursive(cell :: soFar, cellTemplate, standalone.parent)
    }
  }
}
