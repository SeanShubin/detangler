package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Cycle, Module, Standalone}

case class Aggregate(modules: Map[Module, Metrics],
                     dependsOnMap: Map[Standalone, Set[Standalone]],
                     dependedOnByMap: Map[Standalone, Set[Standalone]],
                     cycles: Map[Standalone, Set[Standalone]]) {
  def add(standalone: Standalone): Aggregate = {
    if (modules.contains(standalone)) this
    else {
      if (cycles.contains(standalone)) {
        addCycle(standalone)
      } else {
        addStandalone(standalone)
      }
    }
  }

  private def addCycle(standalone: Standalone): Aggregate = {
    val cycleParts = cycles(standalone)
    val cycleDependsOn = cycleParts.flatMap(dependsOnMap).diff(cycleParts)
    val afterComputingDependsOn = cycleDependsOn.foldLeft(this)(Aggregate.add)
    afterComputingDependsOn.addCycleWithoutComputingDependsOn(cycleParts, cycleDependsOn)
  }

  private def addCycleWithoutComputingDependsOn(cycleParts: Set[Standalone], cycleDependsOn: Set[Standalone]): Aggregate = {
    val depth = if (cycleDependsOn.isEmpty) cycleParts.size else cycleDependsOn.map(modules).map(_.depth).max + cycleParts.size
    val cycleTransitiveDependencies = cycleDependsOn.map(modules).flatMap(_.transitiveDependencies) ++ cycleDependsOn
    val cycleDependedOnBy = cycleParts.flatMap(dependedOnByMap).diff(cycleParts)
    val cycle = Cycle(cycleParts)
    val metrics = Metrics(
      id = cycle,
      children = Set(),
      cycleParts = cycleParts,
      dependsOn = cycleDependsOn,
      dependedOnBy = cycleDependedOnBy,
      depth = depth,
      transitiveDependencies = cycleTransitiveDependencies,
      partOfCycle = None
    )
    val cycleEntry = (cycle, metrics)
    def createMetricsEntryForCyclePart(cyclePart: Standalone): (Module, Metrics) = {
      val directDependencies = dependsOnMap(cyclePart)
      val cyclePartsNotMe = cycleParts.diff(Set(cyclePart))
      val transitiveDependencies = directDependencies ++ cycleTransitiveDependencies ++ cyclePartsNotMe
      val metrics = Metrics(
        id = cyclePart,
        children = Set(),
        cycleParts = cycleParts,
        dependsOn = dependsOnMap(cyclePart),
        dependedOnBy = dependedOnByMap(cyclePart),
        depth = depth,
        transitiveDependencies = transitiveDependencies,
        partOfCycle = Some(cycle)
      )
      (cyclePart, metrics)
    }
    val cyclePartEntries = cycleParts.map(createMetricsEntryForCyclePart)
    val newEntries = cycleEntry +: cyclePartEntries.toSeq
    this.copy(modules = modules ++ newEntries)
  }

  private def addStandalone(standalone: Standalone): Aggregate = {
    val afterComputingDependsOn = dependsOnMap(standalone).foldLeft(this)(Aggregate.add)
    afterComputingDependsOn.addStandaloneWithoutComputingDependsOn(standalone)
  }

  private def addStandaloneWithoutComputingDependsOn(standalone: Standalone): Aggregate = {
    val dependsOn = dependsOnMap(standalone)
    val depth = if (dependsOn.isEmpty) 0 else dependsOn.map(modules).map(_.depth).max + 1
    val transitiveDependencies = dependsOn.map(modules).flatMap(_.transitiveDependencies) ++ dependsOn
    val metrics = Metrics(
      id = standalone,
      children = Set(),
      cycleParts = Set(),
      dependsOn = dependsOn,
      dependedOnBy = dependedOnByMap(standalone),
      depth = depth,
      transitiveDependencies = transitiveDependencies,
      partOfCycle = None
    )
    this.copy(modules = modules.updated(standalone, metrics))
  }

  override def toString: String = {
    val summary = s"${modules.size} modules"
    val modulesString = modules.keys.toSeq.mkString(", ")
    s"$summary, $modulesString"
  }
}

object Aggregate {
  def add(aggregate: Aggregate, standalone: Standalone): Aggregate = {
    aggregate.add(standalone)
  }
}
