package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Cycle

trait CycleTemplateRules {
  def generate(singleTemplate: HtmlElement, cycle: Cycle): HtmlElement
}
