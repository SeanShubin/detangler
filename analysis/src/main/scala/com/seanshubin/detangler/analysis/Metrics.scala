package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Module, Standalone}

case class Metrics(id: Module,
                   children: Set[Module],
                   cycleParts: Set[Standalone],
                   dependsOn: Set[Standalone],
                   dependedOnBy: Set[Standalone],
                   depth: Int,
                   transitiveDependencies: Set[Standalone]) {
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
    transitiveDependencies = Set())
}