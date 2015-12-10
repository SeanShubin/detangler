package com.seanshubin.detangler.core

class PageTemplate(pageModule: Module,
                   detangled: Detangled,
                   template: HtmlFragment) {
  val emptyPage = template.remove(".modules").remove(".reasons")
  val modulesOuter = template.one(".modules").remove(".module").remove(".module-with-cycle")
  val moduleFragment = template.one(".module").remove(".dependency")
  val reasonsFragment = template.one(".reasons")
  val dependencyFragment = template.one(".dependency")
  val moduleCycle = template.one(".module-with-cycle")

  def generate(): HtmlFragment = {
    val modules = modulesOuter.appendAll(".modules", generateModules())
    val reasons = generateReasons()
    emptyPage.appendChild(modules).appendChild(reasons)
  }

  def generateModules(): Seq[HtmlFragment] = {
    detangled.composedOf(pageModule).map(generateModule)
  }

  def generateModule(module: Module): HtmlFragment = {
    val result = if (module.isCycle) {
      val moduleCycleTemplate = new ModuleCycleTemplate(moduleCycle, module, detangled)
      moduleCycleTemplate.generate()
    } else {
      val moduleSummaryTemplate = new ModuleSummaryTemplate(moduleFragment, detangled)
      val dependsOn = generateDependsOn(module)
      val dependedOnBy = generateDependedOnBy(module)
      moduleSummaryTemplate.generate(module).appendChild(dependsOn).appendChild(dependedOnBy)
    }
    result
  }

  def generateDependsOn(module: Module): HtmlFragment = {
    val dependsOnTemplate = new ModuleDependencyTemplate(dependencyFragment, pageModule, module, ReasonDirection.TowardDependsOn, detangled)
    dependsOnTemplate.generate()
  }

  def generateDependedOnBy(module: Module): HtmlFragment = {
    val dependsOnTemplate = new ModuleDependencyTemplate(dependencyFragment, pageModule, module, ReasonDirection.TowardDependedOnBy, detangled)
    dependsOnTemplate.generate()
  }

  def generateReasons(): HtmlFragment = {
    val reasonsTemplate = new ReasonsTemplate(reasonsFragment, pageModule)
    val reasons = detangled.reasonsFor(pageModule)
    val result = reasonsTemplate.generate(reasons)
    result
  }
}
