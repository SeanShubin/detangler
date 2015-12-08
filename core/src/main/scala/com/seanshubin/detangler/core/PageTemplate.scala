package com.seanshubin.detangler.core

class PageTemplate(unit: UnitId,
                   detangled: Detangled,
                   template: HtmlFragment) {
  val emptyPage = template.remove(".units").remove(".reasons")
  val unitsOuter = template.one(".units").remove(".unit")
  val unitFragment = template.one(".unit").remove(".unit-dependency")
  val reasonsFragment = template.one(".reasons")
  val dependencyFragment = template.one(".unit-dependency")

  def generate(): HtmlFragment = {
    val units = unitsOuter.appendAll(".units", generateUnits())
    val reasons = generateReasons()
    emptyPage.appendChild(units).appendChild(reasons)
  }

  def generateUnits(): Seq[HtmlFragment] = {
    detangled.composedOf(unit).map(generateUnit)
  }

  def generateUnit(unitId: UnitId): HtmlFragment = {
    val unitSummaryTemplate = new UnitSummaryTemplate(unitFragment, detangled)
    val dependsOn = generateDependsOn(unitId)
    val dependedOnBy = generateDependedOnBy(unitId)
    val result = unitSummaryTemplate.generate(unitId).appendChild(dependsOn).appendChild(dependedOnBy)
    result
  }

  def generateDependsOn(unitId: UnitId): HtmlFragment = {
    val dependsOnTemplate = new UnitDependencyTemplate(dependencyFragment, unitId, unitId, ArrowDirection.TowardDependsOn, detangled)
    dependsOnTemplate.generate()
  }

  def generateDependedOnBy(unitId: UnitId): HtmlFragment = {
    val dependsOnTemplate = new UnitDependencyTemplate(dependencyFragment, unitId, unitId, ArrowDirection.TowardDependedOnBy, detangled)
    dependsOnTemplate.generate()
  }

  def generateReasons(): HtmlFragment = {
    val reasonsTemplate = new ArrowsTemplate(reasonsFragment, unit)
    val arrows = detangled.arrowsFor(unit)
    val result = reasonsTemplate.generate(arrows)
    result
  }
}
