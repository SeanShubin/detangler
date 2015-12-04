package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class ArrowsTemplate(templateText: String, context: UnitId) {
  private val template = Jsoup.parseBodyFragment(templateText)
  private val reasons: Element = template.select(".reasons").get(0)
  private val reason: Element = reasons.select(".reason").get(0)
  reasons.removeAttr("class")
  reasons.remove()
  reason.removeAttr("class")
  reason.remove()

  def generate(arrows: Seq[Arrow]): String = {
    val result = composeArrows(arrows)
    result.outerHtml()
  }

  private def composeArrows(arrows: Seq[Arrow]): Element = {
    val result = reasons.clone()
    for {
      arrow <- arrows
    } yield {
      val child = composeArrow(arrow)
      result.appendChild(child)
    }
    result
  }

  private def composeArrow(arrow: Arrow): Element = {
    val result = reason.clone()
    result.attr("id", HtmlUtil.arrowId(arrow.from, arrow.to))
    val fromElement = result.select(".from").get(0)
    fromElement.attr("href", HtmlUtil.htmlLink(context, arrow.from))
    fromElement.text(HtmlUtil.htmlName(arrow.from))
    val toElement = result.select(".to").get(0)
    toElement.attr("href", HtmlUtil.htmlLink(context, arrow.to))
    toElement.text(HtmlUtil.htmlName(arrow.to))
    if (arrow.reasons.nonEmpty) {
      val subReasons = composeArrows(arrow.reasons)
      result.appendChild(subReasons)
    }
    result
  }
}
