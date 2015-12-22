package com.seanshubin.detangler.model

trait Detangled {
  def root(): Standalone

  def childModules(standalone: Standalone): Set[Module]

  def childStandalone(standalone: Standalone): Set[Standalone]

  def cycleSize(cycle: Cycle): Int

  def cycleParts(cycle: Cycle): Set[Standalone]

  def depth(module: Module): Int

  def complexity(module: Module): Int

  def dependsOn(module: Module): Set[Standalone]

  def dependedOnBy(module: Module): Set[Standalone]

  def reasonsFor(standalone: Standalone): Set[Reason]

  def isLeaf(standalone: Standalone): Boolean

  def levelsDeep: Int
}
