package com.seanshubin.detangler.core.html

case class HtmlReason(from: HtmlAnchor, to: HtmlAnchor, reasons: Seq[HtmlReason])

