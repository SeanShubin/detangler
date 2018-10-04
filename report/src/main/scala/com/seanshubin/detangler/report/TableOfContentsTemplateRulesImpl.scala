package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Standalone}

import scala.annotation.tailrec

class TableOfContentsTemplateRulesImpl(detangled: Detangled) extends TableOfContentsTemplateRules {
  override def generate(template: HtmlElement): HtmlElement = {
    val empty = template.remove(".table-of-contents-row")
    val rowTemplate = template.select(".table-of-contents-row")

    def appendChild(appendToMe: HtmlElement, child: Standalone): HtmlElement = {
      val text = standaloneToText(child)
      val row = rowTemplate.anchor(".link", HtmlRender.absoluteModuleLink(child), text)
      appendToMe.append("#table-of-contents-body", row)
    }

    val children = detangled.allStandalone()
    children.foldLeft(empty)(appendChild)
  }

  private def standaloneToText(standalone: Standalone): String = {
    standaloneToTextRecursive(Nil, standalone)
  }

  @tailrec
  private def standaloneToTextRecursive(soFar: List[String], standalone: Standalone): String = {
    if (standalone.isRoot) {
      soFar.mkString(".")
    } else {
      val text = HtmlRender.reportPageLinkName(standalone)
      standaloneToTextRecursive(text :: soFar, standalone.parent)
    }
  }
}
