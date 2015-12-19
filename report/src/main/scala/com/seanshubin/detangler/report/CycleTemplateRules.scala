package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Single}

trait CycleTemplateRules {
  def generate(cycleTemplate: HtmlElement, context: Single, cycle: Cycle): HtmlElement
}
