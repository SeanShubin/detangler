package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Single, Cycle}

trait CycleTemplateRules {
  def generate(cycleTemplate: HtmlElement, context:Single, cycle: Cycle): HtmlElement
}
