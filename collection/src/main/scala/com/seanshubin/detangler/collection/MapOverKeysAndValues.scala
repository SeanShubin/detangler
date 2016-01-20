package com.seanshubin.detangler.collection

object MapOverKeysAndValues {
  def map[T,U](map:Map[T, Seq[T]], f:T => U):Map[U, Seq[U]] ={
    for {
      (key, values) <- map
    } yield {
      val newKey = f(key)
      val newValues = values.map(f)
      (newKey, newValues)
    }
  }
}
