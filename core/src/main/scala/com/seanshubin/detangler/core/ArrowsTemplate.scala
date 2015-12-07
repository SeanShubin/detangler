package com.seanshubin.detangler.core

class ArrowsTemplate(template: HtmlFragment, context: UnitId) {
  private val reasons = template.one(".reasons").remove(".reason")
  private val reason = template.one(".reason")

  def generate(arrows: Seq[Arrow]): HtmlFragment = {
    val result = composeArrows(arrows)
    result
  }

  private def composeArrows(arrows: Seq[Arrow]): HtmlFragment = {
    val children = arrows.map(composeArrow)
    val result = reasons.appendAll(".reasons", children)
    result
  }

  private def composeArrow(arrow: Arrow): HtmlFragment = {
    val withoutSubReasons = reason.
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
