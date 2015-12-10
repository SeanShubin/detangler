package com.seanshubin.detangler.core.template

import com.seanshubin.detangler.core.{Detangled, HtmlFragment, UnitId}

class UnitsTemplateRules(unitsTemplate: HtmlFragment, detangled: Detangled, parentUnit: UnitId) {
  private val emptyTemplate = unitsTemplate.remove(".unit").remove(".cycle")
  private val unitTemplate = unitsTemplate.one(".unit")
  private val cycleTemplate = unitsTemplate.one(".cycle")

  def generate(): HtmlFragment = {
    val unitsAndCycles = detangled.composedOf(parentUnit).map(generateUnitOrCycle)
    emptyTemplate.appendAll(".units", unitsAndCycles)
  }

  private def generateUnitOrCycle(unit: UnitId): HtmlFragment = {
    val result = if (unit.isCycle) {
      val cycleTemplateRules = new CycleTemplateRules(cycleTemplate, detangled, parentUnit, unit)
      cycleTemplateRules.generate()
    } else {
      val unitTemplateRules = new UnitTemplateRules(unitTemplate, detangled, parentUnit, unit)
      unitTemplateRules.generate()
    }
    result
  }
}
