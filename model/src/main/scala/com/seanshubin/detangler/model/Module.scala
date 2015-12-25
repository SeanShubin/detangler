package com.seanshubin.detangler.model

import scala.annotation.tailrec

sealed trait Module {
  def isRoot: Boolean

  def parent: Standalone

  def toString: String
}

case class Standalone(path: Seq[String]) extends Module with Ordered[Standalone] {
  override def toString: String = s"${path.mkString("--")}"

  override def compare(that: Standalone): Int = compare(this.path.toList, that.path.toList)

  def parent: Standalone = Standalone(path.init)

  override def isRoot: Boolean = path.isEmpty

  def level: Int = path.size

  def atLevel(targetLevel: Int): Standalone = Standalone(path.take(targetLevel))

  @tailrec
  private def compare(left: List[String], right: List[String]): Int = {
    if (left.size != right.size)
      throw new RuntimeException(s"Cannot compare modules at different scales, got ${left.size} and ${right.size}")
    val size = left.size
    if (size == 0) 0
    else if (left.head == right.head) compare(left.tail, right.tail)
    else left.head.compareTo(right.head)
  }

}

object Standalone {
  val Root = Standalone(Seq())
}

case class Cycle(parts: Set[Standalone]) extends Module {
  require(parts.tail.forall(part => parts.head.parent == part.parent))

  override def isRoot: Boolean = false

  override def parent: Standalone = parts.head.parent

  override def toString: String = {
    s"""${parts.toSeq.sorted.mkString("-")}"""
  }
}
