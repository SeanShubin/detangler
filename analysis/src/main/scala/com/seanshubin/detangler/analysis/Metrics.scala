package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.detangler.model.{Cycle, Module, Standalone}

case class Metrics(id: Module,
                   children: Set[Module],
                   cycleParts: Set[Standalone],
                   dependsOn: Set[Standalone],
                   dependedOnBy: Set[Standalone],
                   depth: Int,
                   transitiveDependencies: Set[Standalone],
                   partOfCycle: Option[Cycle]) {
  override def toString: String = {
    s"id = $id, depth = $depth, children = $children, cycleParts = $cycleParts, dependsOn = $dependsOn, dependedOnBy = $dependedOnBy, transitiveDependencies = $transitiveDependencies"
  }

  def parent: Standalone = id.parent

  def breadth: Int = dependsOn.size

  def transitive: Int = transitiveDependencies.size
}

object Metrics {
  val Empty = Metrics(
    id = Standalone(Seq()),
    children = Set(),
    cycleParts = Set(),
    dependsOn = Set(),
    dependedOnBy = Set(),
    depth = 0,
    transitiveDependencies = Set(),
    partOfCycle = None
  )
  val compare: (Metrics, Metrics) => Int = Compare.mergeCompareFunctions(Compare.reverse(compareDepth), compareId)

  def compareDepth(left: Metrics, right: Metrics): Int = {
    Ordering.Int.compare(left.depth, right.depth)
  }

  def compareId(left: Metrics, right: Metrics): Int = {
    Module.compare(left.id, right.id)
  }
}
