package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._

class ReasonsTemplateRules(reasonsTemplate: HtmlFragment, detangled: Detangled, context: UnitId, arrows: Seq[Arrow]) {
  private val parentTemplate = reasonsTemplate.remove(".reason")
  private val reasonTemplate = reasonsTemplate.one(".reason")

  def generate(): HtmlFragment = {
    val result = composeArrows(arrows)
    result
  }

  private def composeArrows(arrows: Seq[Arrow]): HtmlFragment = {
    val children = arrows.map(composeArrow)
    val result = parentTemplate.appendAll(".reasons", children)
    result
  }

  private def composeArrow(arrow: Arrow): HtmlFragment = {
    val withoutSubReasons = reasonTemplate.
      attr(".reason", "id", HtmlUtil.arrowId(arrow.from, arrow.to)).
      anchor(".from", HtmlUtil.htmlLink(context, arrow.from), HtmlUtil.htmlName(arrow.from)).
      anchor(".to", HtmlUtil.htmlLink(context, arrow.to), HtmlUtil.htmlName(arrow.to))
    val result = if (arrow.reasons.isEmpty) {
      withoutSubReasons
    } else {
      val subReasons = composeArrows(arrow.reasons)
      withoutSubReasons.appendChild(subReasons)
    }
    result
  }
}
