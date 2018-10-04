package com.seanshubin.detangler.model

trait Detangled {
  def cycles(): Seq[Cycle]

  def entryPoints(): Seq[Standalone]

  def allStandalone(): Seq[Standalone]

  def childModules(standalone: Standalone): Seq[Module]

  def childStandalone(standalone: Standalone): Seq[Standalone]

  def cycleSize(cycle: Cycle): Int

  def cycleParts(cycle: Cycle): Seq[Standalone]

  def partOfCycle(standalone: Standalone): Option[Cycle]

  def breadth(module: Module): Int

  def depth(module: Module): Int

  def transitive(module: Module): Int

  def dependsOn(module: Module): Seq[Standalone]

  def dependedOnBy(module: Module): Seq[Standalone]

  def reasonsFor(standalone: Standalone): Seq[Reason]

  def isLeaf(standalone: Standalone): Boolean

  def levelsDeep: Int

  def plainDependsOnFor(standalone: Standalone): Map[String, Set[String]]

  def plainCyclesFor(standalone: Standalone): Map[String, Set[String]]

  def plainEntryPointsFor(standalone: Standalone): Set[String]

  def contains(standalone: Standalone): Boolean
}
