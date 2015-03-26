package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html.HtmlPage

trait ReportTransformer {
  def rootReport(detangled: Detangled): HtmlPage
}
