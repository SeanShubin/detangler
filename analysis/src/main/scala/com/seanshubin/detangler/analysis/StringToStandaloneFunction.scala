package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

class StringToStandaloneFunction(level: Int,
                                 includeStartsWithSeq: Seq[Seq[String]],
                                 excludeStartsWithSeq: Seq[Seq[String]],
                                 dropStartsWithSeq: Seq[Seq[String]],
                                 ignoreJavadoc: Boolean) extends (String => Option[Standalone]) {
  override def apply(originalTarget: String): Option[Standalone] = {
    val target = removePrefix("WEB-INF/classes/", originalTarget)
    val parts = splitIntoParts(target)
    val includeContains =
      includeStartsWithSeq.isEmpty ||
        includeStartsWithSeq.exists(includeStartsWith => parts.startsWith(includeStartsWith))
    val excludeContains =
      excludeStartsWithSeq.nonEmpty &&
        excludeStartsWithSeq.exists(excludeStartsWith => parts.startsWith(excludeStartsWith))
    val ignoreBecauseJavadoc = ignoreJavadoc && isJavadoc(parts)
    if (includeContains && !excludeContains && !ignoreBecauseJavadoc) {
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

  private def isJavadoc(parts: Seq[String]): Boolean = {
    parts.size > 1 && parts(parts.size - 2) == "javadoc"
  }

  private def splitIntoParts(target: String): Seq[String] = {
    dropDollarAndAfter(target).split("/", -1)
  }

  private def dropDollarAndAfter(target: String): String = target.takeWhile(_ != '$')

  private def removePrefix(prefix: String, target: String): String = {
    if (target.startsWith(prefix)) target.substring(prefix.size)
    else target
  }
}

object StringToStandaloneFunction {
  def standaloneAtLevel(level: Int, parts: Seq[String]): Standalone = {
    val size = parts.size
    val standaloneParts = for {
      index <- 0 until level
    } yield {
      if (index < level - 2) {
        if (index > size - 2) {
          if (size > 1) {
            s"root-of-${parts(size - 2)}"
          } else {
            "-root-"
          }
        } else {
          parts(index)
        }
      } else if (index < level - 1) {
        if (level < size + 1) {
          parts.slice(level - 2, size - 1).mkString("/")
        } else {
          if (size > 1) {
            s"root-of-${parts(size - 2)}"
          } else {
            "-root-"
          }
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
