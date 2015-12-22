package com.seanshubin.detangler.analysis

trait CycleFinder[T] {
  def findCycles(graph: Map[T, Set[T]]): Map[T, Set[T]]
}
