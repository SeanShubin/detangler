package com.seanshubin.detangler.analysis

import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}

object CollectionUtil {
  def invert[T](original: Map[T, Set[T]]): Map[T, Set[T]] = {
    val expanded: Seq[(T, T)] = original.toSeq.flatMap(expandPair)
    val reversed: Seq[(T, T)] = expanded.map(reversePair)
    val inverted = reversed.foldLeft(Map[T, Set[T]]())(appendPairToMapFromKeyToSetOfValues)
    inverted
  }

  def expandPair[T, U](pair: (T, Set[U])): Seq[(T, U)] = {
    val (key, values) = pair
    values.toSeq.map(value => (key, value))
  }

  def reversePair[T, U](pair: (T, U)): (U, T) = (pair._2, pair._1)

  def appendPairToMapFromKeyToSetOfValues[T](soFar: Map[T, Set[T]], current: (T, T)): Map[T, Set[T]] = {
    val (key, value) = current
    val newValues = soFar.get(key) match {
      case Some(setOfValues) => setOfValues + value
      case None => Set(value)
    }
    soFar.updated(key, newValues)
  }

  def functionOverPairWithKeyAndSetOfValues[T, U](f: T => U): ((T, Set[T])) => (U, Set[U]) = {
    val newFunctionValue: ((T, Set[T])) => (U, Set[U]) = (pair: (T, Set[T])) => {
      val (key, values) = pair
      val newKey = f(key)
      val newValue = values.map(f)
      val result: (U, Set[U]) = (newKey, newValue)
      result
    }
    newFunctionValue
  }

  def functionOverPairValueOnly[A, B, C](f: B => C): ((A, B)) => (A, C) = (pair: (A, B)) => {
    val (key, value) = pair
    val newValue = f(value)
    val result = (key, newValue)
    result
  }

  def mutableToImmutable[T](mutable: MutableMap[T, MutableSet[T]]): Map[T, Set[T]] = {
    mutable.toMap.map(functionOverPairValueOnly(_.toSet))
  }

  def addEmptyKeysForMapOfSets[T](data: Map[T, Set[T]]) = {
    val individualValues = data.values.flatten

    def addEmptyKeyIfMissing(data: Map[T, Set[T]], item: T): Map[T, Set[T]] = {
      if (data.contains(item)) data else data.updated(item, Set())
    }

    individualValues.foldLeft(data)(addEmptyKeyIfMissing)
  }
}
