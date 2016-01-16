package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}

class CycleTemplateRulesImpl(detangled: Detangled,
                             dependsOnTemplateRules: DependencyTemplateRules,
                             dependedOnByTemplateRules: DependencyTemplateRules) extends CycleTemplateRules {
  override def generate(cycleTemplate: HtmlElement, context: Standalone, cycle: Cycle): HtmlElement = {
    val baseTemplate = cycleTemplate.remove(".cycle-dependency")
    val cycleSummaryTemplate = cycleTemplate.select(".cycle-summary")
    val cycleDetailTemplate = cycleTemplate.select(".cycle-detail")
    val dependencyTemplate = cycleTemplate.select(".cycle-dependency")
    val cycleSummary = generateSummary(cycleSummaryTemplate, cycle)
    val cycleDetail = generateDetail(cycleDetailTemplate, context, cycle)
    val dependsOn = dependsOnTemplateRules.generate(dependencyTemplate, context, cycle)
    val dependedOnBy = dependedOnByTemplateRules.generate(dependencyTemplate, context, cycle)
    val a = baseTemplate.replace(".cycle-summary", cycleSummary)
    val b = a.replace(".cycle-detail", cycleDetail)
    val c = b.replaceOrRemove(".cycle-depends-on", dependsOn)
    val d = c.replaceOrRemove(".cycle-depended-on-by", dependedOnBy)
    d
  }

  def generateSummary(summaryTemplate: HtmlElement, cycle: Cycle): HtmlElement = {
    summaryTemplate.
      attr(".cycle-summary", "id", HtmlRender.id(cycle)).
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
    val link = HtmlRender.moduleLink(context, part)
    val name = HtmlRender.moduleLinkName(part)
    cyclePartTemplate.anchor(".name", link, name)
  }
}
