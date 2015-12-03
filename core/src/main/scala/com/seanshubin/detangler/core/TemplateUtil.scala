package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

object TemplateUtil {
  def arrows(templateText: String, context: UnitId, arrows: Seq[Arrow]): String = {
    val template = Jsoup.parseBodyFragment(templateText)
    val reasons: Element = template.select(".reasons").get(0)
    reasons.removeAttr("class")
    reasons.remove()
    val reason: Element = reasons.select(".reason").get(0)
    reason.removeAttr("class")
    reason.remove()
    val result = composeArrows(reasons, reason, context, arrows)
    result.outerHtml()
  }

  private def composeArrows(originalReasons: Element, originalReason: Element, context: UnitId, arrows: Seq[Arrow]): Element = {
    val result = originalReasons.clone()
    for {
      arrow <- arrows
    } yield {
      val child = composeArrow(originalReasons, originalReason, context, arrow)
      result.appendChild(child)
    }
    result
  }

  private def composeArrow(originalReasons: Element, originalReason: Element, context: UnitId, arrow: Arrow): Element = {
    val result = originalReason.clone()
    result.attr("id", HtmlUtil.arrowId(arrow.from, arrow.to))
    val fromElement = result.select(".from").get(0)
    //    fromElement.removeAttr("class")
    fromElement.attr("href", HtmlUtil.htmlLink(context, arrow.from))
    fromElement.text(HtmlUtil.htmlName(arrow.from))
    val toElement = result.select(".to").get(0)
    //    toElement.removeAttr("class")
    toElement.attr("href", HtmlUtil.htmlLink(context, arrow.to))
    toElement.text(HtmlUtil.htmlName(arrow.to))
    if (arrow.reasons.nonEmpty) {
      val subReasons = composeArrows(originalReasons, originalReason, context, arrow.reasons)
      result.appendChild(subReasons)
    }
    result
  }
}
