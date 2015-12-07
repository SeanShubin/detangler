package com.seanshubin.detangler.core

class UnitSummaryTemplate(templateText: String, detangled: Detangled) {
  private val template = HtmlFragment.fromText(templateText)

  def generate(unit: UnitId): String = {
    template.
      attr(".unit-summary", "id", HtmlUtil.htmlId(unit)).
      text(".name", HtmlUtil.htmlName(unit)).
      text(".depth", detangled.depth(unit).toString).
      text(".complexity", detangled.complexity(unit).toString).
      updateAnchor(".composed-of", HtmlUtil.fileNameFor(unit), "parts").
      text
  }
}
