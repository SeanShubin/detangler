package com.seanshubin.detangler.core

trait Detangled {
  def depth(module: Module): Int

  def complexity(module: Module): Int

  def children(module: Module): Seq[Module]

  def cycleParts(module: Module): Seq[Module]

  def dependsOn(context: Module, module: Module): Seq[Module]

  def dependedOnBy(context: Module, module: Module): Seq[Module]

  def reasonsFor(module: Module): Seq[Reason]

  def reasonsFor(parts: Seq[Module]): Seq[Reason]

  def cycleSize(module: Module): Int
}
