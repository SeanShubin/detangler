package com.seanshubin.detangler.model

trait Detangled {
  def root(): Single

  def childModules(single: Single): Set[Module]

  def childSingles(single: Single): Set[Single]

  def cycleSize(cycle: Cycle): Int

  def cycleParts(cycle: Cycle): Set[Single]

  def depth(module: Module): Int

  def complexity(module: Module): Int

  def dependsOn(context: Single, single: Single): Set[Single]

  def dependedOnBy(context: Single, single: Single): Set[Single]

  def reasonsFor(single: Single): Set[Reason]
}
