package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}

class CycleTemplateRulesImpl(detangled: Detangled) extends CycleTemplateRules {
  override def generate(cycleTemplate: HtmlElement, context: Standalone, cycle: Cycle): HtmlElement = {
    val cycleSummaryTemplate = cycleTemplate.select(".cycle-summary")
    val cycleDetailTemplate = cycleTemplate.select(".cycle-detail")
    val cycleSummary = generateSummary(cycleSummaryTemplate, cycle)
    val cycleDetail = generateDetail(cycleDetailTemplate, context, cycle)
    val result = cycleTemplate.replace(".cycle-summary", cycleSummary).replace(".cycle-detail", cycleDetail)
    result
  }

  def generateSummary(summaryTemplate: HtmlElement, cycle: Cycle): HtmlElement = {
    summaryTemplate.
      text(".size", detangled.cycleSize(cycle).toString).
      text(".depth", detangled.depth(cycle).toString).
      text(".breadth", detangled.breadth(cycle).toString).
      text(".transitive", detangled.transitive(cycle).toString)
  }

  def generateDetail(detailTemplate: HtmlElement, context: Standalone, cycle: Cycle): HtmlElement = {
    val cyclePartTemplate = detailTemplate.select(".cycle-part")
    def generateCyclePartFunction(part: Standalone) = generateCyclePart(cyclePartTemplate, context, part)
    val cycleParts = detangled.cycleParts(cycle).map(generateCyclePartFunction)
    val result = detailTemplate.remove(".cycle-part").append(".append-cycle-part", cycleParts)
    result
  }

  def generateCyclePart(cyclePartTemplate: HtmlElement, context: Standalone, part: Standalone): HtmlElement = {
    cyclePartTemplate.anchor(".name", HtmlRendering.htmlLink(context, part), HtmlRendering.htmlName(part))
  }
}
