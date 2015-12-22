package com.seanshubin.detangler.analysis

import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

class CycleFinderWarshall[T] extends CycleFinder[T] {
  override def findCycles(graph: Map[T, Set[T]]): Map[T, Set[T]] = {
    val matrix = WarshallMatrix.fromGraph(graph)
    matrix.traverse()
    val cycles = matrix.cycles()
    cycles
  }

  private class WarshallMatrix(graph: Map[T, Set[T]], nodes: Seq[T], matrix: Array[Array[Int]]) {
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

    def cycles(): Map[T, Set[T]] = {
      val cycleMap: MutableMap[Int, MutableSet[Int]] = MutableMap()
      for {
        i <- 0 until size
        j <- i + 1 until size
        if matrix(i)(j) == 1 && matrix(j)(i) == 1
      } {
        val value = cycleMap.get(i) match {
          case Some(existing) => existing
          case None =>
            val created = MutableSet[Int]()
            created.add(i)
            cycleMap.put(i, created)
            created
        }
        value.add(j)
        cycleMap.put(j, value)
      }
      val immutableIndicesMap = cycleMap.toMap.map(CollectionUtil.functionOverPairValueOnly(_.toSet))
      val immutableValuesMap = immutableIndicesMap.map(CollectionUtil.functionOverPairWithKeyAndSetOfValues(nodes))
      immutableValuesMap
    }
  }

  private object WarshallMatrix {
    def fromGraph(graph: Map[T, Set[T]]): WarshallMatrix = {
      val nodes = graph.keySet.toSeq
      val size = nodes.size
      val matrix: Array[Array[Int]] = Array.ofDim[Int](size, size)
      new WarshallMatrix(graph, nodes, matrix)
    }
  }

}
