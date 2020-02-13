package com.seanshubin.detangler.compare

import scala.annotation.tailrec

object Compare {
  def reverse[T](compareFunction: (T, T) => Int): (T, T) => Int = (x, y) => -compareFunction(x, y)

  def lessThan[T](compareFunctions: ((T, T) => Int)*): (T, T) => Boolean = {
    lessThan(mergeCompareFunctions(compareFunctions: _*))
  }

  def lessThan[T](compareFunction: (T, T) => Int): (T, T) => Boolean = {
    def newLessThan(x: T, y: T): Boolean = {
      compareFunction(x, y) < 0
    }

    newLessThan
  }

  def mergeCompareFunctions[T](compareFunctions: ((T, T) => Int)*): (T, T) => Int = {
    @tailrec
    def recursiveCompare(compareFunctionsList: List[(T, T) => Int], x: T, y: T): Int = {
      if (compareFunctionsList.isEmpty) 0
      else {
        val headResult = compareFunctionsList.head(x, y)
        if (headResult == 0) {
          recursiveCompare(compareFunctionsList.tail, x, y)
        } else {
          headResult
        }
      }
    }

    def mergedCompareFunction(x: T, y: T): Int = {
      recursiveCompare(compareFunctions.toList, x, y)
    }

    mergedCompareFunction
  }

  def composeCompareSeqFunction[T](compareFunction: (T, T) => Int): (List[T], List[T]) => Int = {
    def compareSeqFunction(left: List[T], right: List[T]): Int = {
      (left, right) match {
        case (Nil, Nil) => 0
        case (Nil, rightHead :: rightTail) => -1
        case (leftHead :: leftTail, Nil) => 1
        case (leftHead :: leftTail, rightHead :: rightTail) =>
          val compareResult = compareFunction(leftHead, rightHead)
          if (compareResult == 0) {
            compareSeqFunction(leftTail, rightTail)
          } else {
            compareResult
          }
      }
    }

    compareSeqFunction
  }

  def mergeComparisonResults(results: Seq[Int]): Int = results.find(_ != 0) match {
    case Some(x) => x
    case None => 0
  }
}
