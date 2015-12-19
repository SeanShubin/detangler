package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Detangled, Reason, Standalone}

class ReasonsTemplateRulesImpl(detangled: Detangled) extends ReasonsTemplateRules {
  override def generate(reasonsTemplate: HtmlElement, context: Standalone, standalone: Standalone): HtmlElement = {
    val baseTemplate = reasonsTemplate.remove(".reason")
    val reasonTemplate = reasonsTemplate.select(".reason")
    val reasons = detangled.reasonsFor(standalone)
    composeReasons(baseTemplate, reasonTemplate, context, reasons)
  }

  def composeReasons(baseTemplate: HtmlElement, reasonTemplate: HtmlElement, context: Standalone, reasons: Set[Reason]): HtmlElement = {
    def composeReasonFunction(reason: Reason) = composeReason(baseTemplate, reasonTemplate, context, reason)
    val childElements = reasons.map(composeReasonFunction)
    val result = baseTemplate.append(".append-reason", childElements)
    result
  }

  def composeReason(baseTemplate: HtmlElement, reasonTemplate: HtmlElement, context: Standalone, reason: Reason): HtmlElement = {
    val withoutSubReasons = reasonTemplate.attr(".reason", "id", HtmlRendering.reasonId(reason.from, reason.to)).
      anchor(".from", HtmlRendering.htmlLink(context, reason.from), HtmlRendering.htmlName(reason.from)).
      anchor(".to", HtmlRendering.htmlLink(context, reason.to), HtmlRendering.htmlName(reason.to))
    val result = if (reason.reasons.isEmpty) {
      withoutSubReasons
    } else {
      val subReasons = composeReasons(baseTemplate, reasonTemplate, context, reason.reasons)
      withoutSubReasons.append(".append-reasons", subReasons)
    }
    result
  }
}
