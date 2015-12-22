package com.seanshubin.detangler.analysis

trait CycleFinder {
  def findCycles(graph: Map[String, Set[String]]): Set[Set[String]]
}
