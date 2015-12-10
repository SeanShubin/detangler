package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core._

class ModuleSummaryTemplateRules(summaryTemplate: HtmlFragment, detangled: Detangled, module: Module) {
  def generate(): HtmlFragment = {
    summaryTemplate.
      attr(".summary", "id", HtmlUtil.htmlId(module)).
      text(".name", HtmlUtil.htmlName(module)).
      text(".depth", detangled.depth(module).toString).
      text(".complexity", detangled.complexity(module).toString).
      anchor(".composed-of", HtmlUtil.fileNameFor(module), "parts")
  }
}
