package com.seanshubin.detangler.analysis

import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

class CycleFinderWarshall extends CycleFinder {
  override def findCycles(graph: Map[String, Set[String]]): Set[Set[String]] = {
    val matrix = WarshallMatrix.fromGraph(graph)
    matrix.traverse()
    val cycles = matrix.cycles()
    cycles
  }

  private class WarshallMatrix(graph: Map[String, Set[String]], nodes: Seq[String], matrix: Array[Array[Int]]) {
    val size = nodes.size

    def traverse(): Unit = {
      for {
        i <- 0 until size
        j <- 0 until size
        if graph(nodes(i)).contains(nodes(j))
      } {
        matrix(i)(j) = 1
      }
      for {
        k <- 0 until size
        i <- 0 until size
        j <- 0 until size
        if matrix(i)(j) == 0
      } {
        matrix(i)(j) = matrix(i)(k) * matrix(k)(j)
      }
    }

    def cycles(): Set[Set[String]] = {
      val inCycles: MutableSet[Int] = MutableSet[Int]()
      val cyclesByFirstInCycle: MutableMap[Int, MutableSet[Int]] = MutableMap()
      for {
        i <- 0 until size
        if !inCycles.contains(i)
        j <- i + 1 until size
        if !inCycles.contains(j)
        if matrix(i)(j) == 1 && matrix(j)(i) == 1
      } {
        inCycles.add(i)
        inCycles.add(j)
        if (!cyclesByFirstInCycle.contains(i)) {
          cyclesByFirstInCycle.put(i, MutableSet())
        }
        cyclesByFirstInCycle(i).add(i)
        cyclesByFirstInCycle(i).add(j)
      }
      val immutableIndicesMap = cyclesByFirstInCycle.toMap.map(CollectionUtil.functionOverPairValueOnly(_.toSet))
      val immutableValuesMap = immutableIndicesMap.map(CollectionUtil.functionOverPairWithKeyAndSetOfValues(nodes))
      val result = immutableValuesMap.values.toSet
      result
    }
  }

  private object WarshallMatrix {
    def fromGraph(graph: Map[String, Set[String]]): WarshallMatrix = {
      val nodes = graph.keySet.toSeq.sorted
      val size = nodes.size
      val matrix: Array[Array[Int]] = Array.ofDim[Int](size, size)
      new WarshallMatrix(graph, nodes, matrix)
    }
  }

}
