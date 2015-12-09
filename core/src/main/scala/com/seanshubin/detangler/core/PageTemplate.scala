package com.seanshubin.detangler.core

class PageTemplate(pageUnit: UnitId,
                   detangled: Detangled,
                   template: HtmlFragment) {
  val emptyPage = template.remove(".units").remove(".reasons")
  val unitsOuter = template.one(".units").remove(".unit").remove(".unit-with-cycle")
  val unitFragment = template.one(".unit").remove(".dependency")
  val reasonsFragment = template.one(".reasons")
  val dependencyFragment = template.one(".dependency")
  val unitCycle = template.one(".unit-with-cycle")

  def generate(): HtmlFragment = {
    val units = unitsOuter.appendAll(".units", generateUnits())
    val reasons = generateReasons()
    emptyPage.appendChild(units).appendChild(reasons)
  }

  def generateUnits(): Seq[HtmlFragment] = {
    detangled.composedOf(pageUnit).map(generateUnit)
  }

  def generateUnit(unitId: UnitId): HtmlFragment = {
    val result = if (unitId.isCycle) {
      val unitCycleTemplate = new UnitCycleTemplate(unitCycle, unitId, detangled)
      unitCycleTemplate.generate()
    } else {
      val unitSummaryTemplate = new UnitSummaryTemplate(unitFragment, detangled)
      val dependsOn = generateDependsOn(unitId)
      val dependedOnBy = generateDependedOnBy(unitId)
      unitSummaryTemplate.generate(unitId).appendChild(dependsOn).appendChild(dependedOnBy)
    }
    result
  }

  def generateDependsOn(unitId: UnitId): HtmlFragment = {
    val dependsOnTemplate = new UnitDependencyTemplate(dependencyFragment, pageUnit, unitId, ArrowDirection.TowardDependsOn, detangled)
    dependsOnTemplate.generate()
  }

  def generateDependedOnBy(unitId: UnitId): HtmlFragment = {
    val dependsOnTemplate = new UnitDependencyTemplate(dependencyFragment, pageUnit, unitId, ArrowDirection.TowardDependedOnBy, detangled)
    dependsOnTemplate.generate()
  }

  def generateReasons(): HtmlFragment = {
    val reasonsTemplate = new ArrowsTemplate(reasonsFragment, pageUnit)
    val arrows = detangled.arrowsFor(pageUnit)
    val result = reasonsTemplate.generate(arrows)
    result
  }
}
