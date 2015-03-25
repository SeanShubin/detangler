package com.seanshubin.detangler.core.html

case class HtmlUnitSummaryPart(caption: String,
                               summaryFragment: String,
                               summaryLinkName: String,
                               depth: String,
                               complexity: String,
                               composedOf: HtmlAnchor)
