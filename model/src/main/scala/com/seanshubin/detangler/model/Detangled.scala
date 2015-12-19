package com.seanshubin.detangler.model

trait Detangled {
  def root(): Standalone

  def childModules(standalone: Standalone): Set[Module]

  def childStandalone(standalone: Standalone): Set[Standalone]

  def cycleSize(cycle: Cycle): Int

  def cycleParts(cycle: Cycle): Set[Standalone]

  def depth(module: Module): Int

  def complexity(module: Module): Int

  def dependsOn(context: Standalone, standalone: Standalone): Set[Standalone]

  def dependedOnBy(context: Standalone, standalone: Standalone): Set[Standalone]

  def reasonsFor(standalone: Standalone): Set[Reason]
}
