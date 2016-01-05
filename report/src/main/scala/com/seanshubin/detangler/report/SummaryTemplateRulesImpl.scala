package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}

class SummaryTemplateRulesImpl(detangled: Detangled) extends SummaryTemplateRules {
  override def generate(template: HtmlElement, entryPoints: Seq[Standalone], cycles: Seq[Cycle]): HtmlElement = {
    val entryPointsFragment = generateEntryPoints(template, entryPoints)
    val cyclesFragment = generateCycles(template, cycles)
    template.replace(".entry-points", entryPointsFragment).replace(".cycles", cyclesFragment)
  }

  private def generateCycles(template: HtmlElement, cycles: Seq[Cycle]): HtmlElement = {
    val emptyCyclesFragment = template.select(".cycles").remove(".cycle")
    val cycleTemplate = template.select(".cycle")
    def generateCycleFunction(cycle: Cycle): HtmlElement = generateCycle(cycleTemplate, cycle)
    val cycleFragments = cycles.map(generateCycleFunction)
    val cyclesFragment = emptyCyclesFragment.append(".append-cycle", cycleFragments)
    cyclesFragment
  }

  private def generateCycle(cycleTemplate: HtmlElement, cycle: Cycle): HtmlElement = {
    val emptyCycleTemplate = cycleTemplate.remove(".cycle-part")
    val cyclePartTemplate = cycleTemplate.select(".cycle-part")
    def generateCyclePartFunction(cyclePart: Standalone): HtmlElement = generateCyclePart(cyclePartTemplate, cyclePart)
    val cyclePartFragments = cycle.parts.map(generateCyclePartFunction)
    val cycleFragment = emptyCycleTemplate.append(".append-cycle-part", cyclePartFragments)
    cycleFragment
  }

  private def generateCyclePart(cyclePartTemplate: HtmlElement, cyclePart: Standalone): HtmlElement = {
    cyclePartTemplate.
      anchor(".name", HtmlRendering.outerHtmlLinkFor(cyclePart), HtmlRendering.htmlName(cyclePart)).
      text(".depth", detangled.depth(cyclePart).toString).
      text(".breadth", detangled.depth(cyclePart).toString).
      text(".transitive", detangled.depth(cyclePart).toString)
  }

  private def generateEntryPoints(template: HtmlElement, entryPoints: Seq[Standalone]): HtmlElement = {
    val emptyEntryPointsFragment = template.select(".entry-points").remove(".entry-point")
    val entryPointTemplate = template.select(".entry-point")
    val entryPointsFragment = emptyEntryPointsFragment.append(".append-entry-point", generateEntryPointElements(entryPointTemplate, entryPoints))
    entryPointsFragment
  }

  private def generateEntryPointElements(template: HtmlElement, entryPoints: Seq[Standalone]): Iterable[HtmlElement] = {
    def generateEntryPointFunction(standalone: Standalone): HtmlElement = {
      generateEntryPoint(template, standalone)
    }
    entryPoints.map(generateEntryPointFunction)
  }

  private def generateEntryPoint(template: HtmlElement, standalone: Standalone): HtmlElement = {
    template.
      anchor(".name", HtmlRendering.outerHtmlLinkFor(standalone), HtmlRendering.htmlName(standalone)).
      text(".depth", detangled.depth(standalone).toString).
      text(".breadth", detangled.depth(standalone).toString).
      text(".transitive", detangled.depth(standalone).toString)
  }
}
