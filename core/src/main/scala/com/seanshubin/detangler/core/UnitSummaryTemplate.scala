package com.seanshubin.detangler.core

class UnitSummaryTemplate(template: HtmlFragment, detangled: Detangled) {
  def generate(unit: UnitId): HtmlFragment = {
    template.
      attr(".unit-summary", "id", HtmlUtil.htmlId(unit)).
      text(".name", HtmlUtil.htmlName(unit)).
      text(".depth", detangled.depth(unit).toString).
      text(".complexity", detangled.complexity(unit).toString).
      anchor(".composed-of", HtmlUtil.fileNameFor(unit), "parts")
  }
}
