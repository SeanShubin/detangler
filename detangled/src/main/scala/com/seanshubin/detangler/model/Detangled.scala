package com.seanshubin.detangler.model

trait Detangled {
  def childModules(standalone: Standalone): Seq[Module]

  def childStandalone(standalone: Standalone): Seq[Standalone]

  def cycleSize(cycle: Cycle): Int

  def cycleParts(cycle: Cycle): Seq[Standalone]

  def breadth(module: Module): Int

  def depth(module: Module): Int

  def transitive(module: Module): Int

  def dependsOn(module: Module): Seq[Standalone]

  def dependedOnBy(module: Module): Seq[Standalone]

  def reasonsFor(standalone: Standalone): Seq[Reason]

  def isLeaf(standalone: Standalone): Boolean

  def levelsDeep: Int
}
