package com.seanshubin.detangler.core

class ReasonsTemplate(template: HtmlFragment, context: UnitId) {
  private val reasonsTemplate = template.one(".reasons").remove(".reason")
  private val reasonTemplate = template.one(".reason")

  def generate(reasons: Seq[Reason]): HtmlFragment = {
    val result = composeReasons(reasons)
    result
  }

  private def composeReasons(reasons: Seq[Reason]): HtmlFragment = {
    val children = reasons.map(composeReason)
    val result = reasonsTemplate.appendAll(".reasons", children)
    result
  }

  private def composeReason(reason: Reason): HtmlFragment = {
    val withoutSubReasons = reasonTemplate.
      attr(".reason", "id", HtmlUtil.reasonId(reason.from, reason.to)).
      anchor(".from", HtmlUtil.htmlLink(context, reason.from), HtmlUtil.htmlName(reason.from)).
      anchor(".to", HtmlUtil.htmlLink(context, reason.to), HtmlUtil.htmlName(reason.to))
    val result = if (reason.reasons.isEmpty) {
      withoutSubReasons
    } else {
      val subReasons = composeReasons(reason.reasons)
      withoutSubReasons.appendChild(subReasons)
    }
    result
  }
}
