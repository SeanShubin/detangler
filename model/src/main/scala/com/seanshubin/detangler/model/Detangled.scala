package com.seanshubin.detangler.model

trait Detangled {
  def root(): Single

  def children(single: Single): Set[Module]

  def depth(module: Module): Int

  def complexity(module: Module): Int

  def dependsOn(single: Single): Set[Single]

  def dependedOnBy(single: Single): Set[Single]
}
