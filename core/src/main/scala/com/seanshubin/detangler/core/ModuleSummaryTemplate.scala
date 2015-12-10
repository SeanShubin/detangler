package com.seanshubin.detangler.core

class ModuleSummaryTemplate(template: HtmlFragment, detangled: Detangled) {
  def generate(module: Module): HtmlFragment = {
    template.
      attr(".summary", "id", HtmlUtil.htmlId(module)).
      text(".name", HtmlUtil.htmlName(module)).
      text(".depth", detangled.depth(module).toString).
      text(".complexity", detangled.complexity(module).toString).
      anchor(".composed-of", HtmlUtil.fileNameFor(module), "parts")
  }
}
