package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Standalone}

class SummaryTemplateRulesStub extends SummaryTemplateRules {
  override def generate(template: HtmlElement, entryPoints: Seq[Standalone], cycles: Seq[Cycle]): HtmlElement = ???
}
