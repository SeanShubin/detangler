package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Detangled, Standalone}

class SummaryTemplateRulesImpl(detangled: Detangled, allowedCycles: Set[Standalone]) extends SummaryTemplateRules {
  override def generate(template: HtmlElement, entryPoints: Seq[Standalone], cycles: Seq[Cycle]): HtmlElement = {
    val tableOfContentsFragment = generateTableOfContents(template)
    val entryPointsFragment = generateEntryPoints(template, entryPoints)
    val cyclesFragment = generateCycles(template, cycles)
    val newCyclesFragment = generateNewCycles(template, cycles)
    template.
      replaceOrRemove(".new-cycles", newCyclesFragment).
      replace(".table-of-contents", tableOfContentsFragment).
      replace(".entry-points", entryPointsFragment).
      replace(".cycles", cyclesFragment)
  }

  private def generateTableOfContents(template: HtmlElement): HtmlElement = {
    val tableOfContentsFragment = template.select(".table-of-contents")
    val rootLink = HtmlRender.reportPageLink(Standalone.Root)
    val rootName = HtmlRender.reportPageLinkName(Standalone.Root)
    tableOfContentsFragment.anchor(".root", rootLink, rootName)
  }

  private def generateNewCycles(template: HtmlElement, cycles: Seq[Cycle]): Option[HtmlElement] = {
    val newCycleParts = cycles.flatMap(_.parts).filterNot(allowedCycles.contains)
    if(newCycleParts.isEmpty){
      None
    } else{
      val emptyTemplate = template.select(".new-cycles").remove(".new-cycle-part")
      val rowTemplate = template.select(".new-cycle-part")
      def generateNewCycleFunction(standalone: Standalone): HtmlElement = generateNewCycle(rowTemplate, standalone)
      val fragments = newCycleParts.map(generateNewCycleFunction)
      val cyclesFragment = emptyTemplate.append(".append-new-cycle-part", fragments)
      Some(cyclesFragment)
    }
  }

  private def generateNewCycle(template: HtmlElement, standalone: Standalone): HtmlElement = {
    val cycle = detangled.partOfCycle(standalone).get
    template.
      attr(".cycle-link", "href", HtmlRender.absoluteModuleLink(cycle)).
      anchor(".name", HtmlRender.absoluteModuleLink(standalone), HtmlRender.standaloneLinkQualifiedName(standalone)).
      text(".depth", detangled.depth(standalone).toString).
      text(".breadth", detangled.breadth(standalone).toString).
      text(".transitive", detangled.transitive(standalone).toString).
      anchor(".graph", HtmlRender.graphLink(standalone.parent), HtmlRender.graphLinkName(standalone.parent))
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
    val parentLink = HtmlRender.reportPageLink(cycle.parent)
    val parentName = HtmlRender.standaloneLinkQualifiedName(cycle.parent)
    val cycleText = s"${cycle.parts.size} part cycle"
    val cycleLink = HtmlRender.absoluteModuleLink(cycle)
    val graphText = HtmlRender.graphLinkName(cycle.parent)
    val graphLink = HtmlRender.graphLink(cycle.parent)
    val cycleFragment = emptyCycleTemplate.
      anchor(".cycle-link", cycleLink, cycleText).
      anchor(".graph-link", graphLink, graphText).
      text(".level", cycle.level.toString).
      anchor(".parent-link", parentLink, parentName).
      append(".append-cycle-part", cyclePartFragments)
    cycleFragment
  }

  private def generateCyclePart(cyclePartTemplate: HtmlElement, cyclePart: Standalone): HtmlElement = {
    val link = HtmlRender.absoluteModuleLink(cyclePart)
    val name = HtmlRender.moduleLinkName(cyclePart)
    cyclePartTemplate.
      anchor(".name", link, name)
  }

  private def generateEntryPoints(template: HtmlElement, entryPoints: Seq[Standalone]): HtmlElement = {
    val emptyEntryPointsFragment = template.select(".entry-points").remove(".entry-point")
    val entryPointTemplate = template.select(".entry-point")
    val entryPointsFragment = emptyEntryPointsFragment.append(
      ".append-entry-point", generateEntryPointElements(entryPointTemplate, entryPoints))
    entryPointsFragment
  }

  private def generateEntryPointElements(template: HtmlElement, entryPoints: Seq[Standalone]): Iterable[HtmlElement] = {
    def generateEntryPointFunction(standalone: Standalone): HtmlElement = {
      generateEntryPoint(template, standalone)
    }
    entryPoints.map(generateEntryPointFunction)
  }

  private def generateEntryPoint(template: HtmlElement, standalone: Standalone): HtmlElement = {
    val link = HtmlRender.absoluteModuleLink(standalone)
    val name = HtmlRender.standaloneLinkQualifiedName(standalone)
    template.
      anchor(".name", link, name).
      text(".depth", detangled.depth(standalone).toString).
      text(".breadth", detangled.breadth(standalone).toString).
      text(".transitive", detangled.transitive(standalone).toString).
      anchor(".graph", HtmlRender.graphLink(standalone.parent), HtmlRender.graphLinkName(standalone.parent))
  }
}
