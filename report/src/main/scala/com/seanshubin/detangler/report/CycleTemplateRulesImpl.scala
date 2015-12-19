package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Single}

class CycleTemplateRulesImpl(detangled: Detangled) extends CycleTemplateRules {
  override def generate(cycleTemplate: HtmlElement, context: Single, cycle: Cycle): HtmlElement = {
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
      text(".complexity", detangled.complexity(cycle).toString)
  }

  def generateDetail(detailTemplate: HtmlElement, context: Single, cycle: Cycle): HtmlElement = {
    val cyclePartTemplate = detailTemplate.select(".cycle-part")
    def generateCyclePartFunction(part: Single) = generateCyclePart(cyclePartTemplate, context, part)
    val cycleParts = detangled.cycleParts(cycle).map(generateCyclePartFunction)
    val result = detailTemplate.remove(".cycle-part").append(".append-cycle-part", cycleParts)
    result
  }

  def generateCyclePart(cyclePartTemplate: HtmlElement, context: Single, part: Single): HtmlElement = {
    cyclePartTemplate.anchor(".name", HtmlUtil.htmlLink(context, part), HtmlUtil.htmlName(part))
  }
}
