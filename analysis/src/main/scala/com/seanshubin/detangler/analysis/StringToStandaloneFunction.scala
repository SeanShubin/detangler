package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

class StringToStandaloneFunction(level: Int,
                                 includeStartsWithSeq: Seq[Seq[String]],
                                 dropStartsWithSeq: Seq[Seq[String]]) extends (String => Option[Standalone]) {
  override def apply(target: String): Option[Standalone] = {
    val parts = splitIntoParts(target)
    if (includeStartsWithSeq.exists(includeStartsWith => parts.startsWith(includeStartsWith))) {
      val trimmedParts = dropStartsWithSeq.find(dropStartsWith => parts.startsWith(dropStartsWith)) match {
        case Some(drop) => parts.drop(drop.size)
        case None => parts
      }
      val standalone = StringToStandaloneFunction.standaloneAtLevel(level, trimmedParts)
      Some(standalone)
    } else {
      None
    }
  }

  private def splitIntoParts(target: String): Seq[String] = {
    dropDollarAndAfter(target).split("/", -1)
  }

  private def dropDollarAndAfter(target: String): String = target.takeWhile(_ != '$')
}

object StringToStandaloneFunction {
  def standaloneAtLevel(level: Int, parts: Seq[String]): Standalone = {
    val size = parts.size
    val standaloneParts = for {
      index <- 0 until level
    } yield {
        if (index < level - 2) {
          if (index > size - 2) {
            s"placeholder-level-${index + 1}"
          } else {
            parts(index)
          }
        } else if (index < level - 1) {
          if (level < size + 1) {
            parts.slice(level - 2, size - 1).mkString("/")
          } else {
            s"placeholder-level-${index + 1}"
          }
        } else {
          if (level < 2) {
            parts.mkString("/")
          } else {
            parts.last
          }
        }
      }
    Standalone(standaloneParts)
  }
}
