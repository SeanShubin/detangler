package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Standalone}

trait CycleTemplateRules {
  def generate(cycleTemplate: HtmlElement, context: Standalone, cycle: Cycle): HtmlElement
}
