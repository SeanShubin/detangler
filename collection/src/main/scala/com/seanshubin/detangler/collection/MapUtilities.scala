package com.seanshubin.detangler.collection

object MapUtilities {
  def map[T, U](map: Map[T, Seq[T]], f: T => U): Map[U, Seq[U]] = {
    for {
      (key, values) <- map
    } yield {
      val newKey = f(key)
      val newValues = values.map(f)
      (newKey, newValues)
    }
  }

  def merge[T](left: Map[T, Set[T]], right: Map[T, Set[T]]): Map[T, Set[T]] = {
    val allKeys = left.keySet ++ right.keySet
    val newEntries = for {
      key <- allKeys
    } yield {
      val mergedValues = left.getOrElse(key, Set()) ++ right.getOrElse(key, Set())
      (key, mergedValues)
    }
    newEntries.toMap
  }
}
