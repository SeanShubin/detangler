package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Standalone}

trait SummaryTemplateRules {
  def generate(template: HtmlElement, entryPoints: Seq[Standalone], cycles: Seq[Cycle]): HtmlElement
}
