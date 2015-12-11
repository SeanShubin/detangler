package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._
import com.seanshubin.detangler.core.template.FragmentSelectors._

class ReasonsTemplateRules(reasonsTemplate: HtmlFragment, detangled: Detangled, context: Module, reasons: Seq[Reason]) {
  private val parentTemplate = reasonsTemplate.remove(SelectorReason)
  private val reasonTemplate = reasonsTemplate.one(SelectorReason)

  def generate(): HtmlFragment = {
    val result = composeReasons(reasons)
    result
  }

  private def composeReasons(reasons: Seq[Reason]): HtmlFragment = {
    val children = reasons.map(composeReason)
    val result = parentTemplate.appendAll(SelectorReasons, children)
    result
  }

  private def composeReason(reason: Reason): HtmlFragment = {
    val withoutSubReasons = reasonTemplate.
      attr(SelectorReason, "id", HtmlUtil.reasonId(reason.from, reason.to)).
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
