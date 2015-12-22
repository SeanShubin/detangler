package com.seanshubin.detangler.model

import scala.annotation.tailrec

sealed trait Module {
  def parent: Standalone

  def toString: String
}

case class Standalone(path: Seq[String]) extends Module with Ordered[Standalone] {
  override def toString: String = s"${path.mkString("--")}"

  override def compare(that: Standalone): Int = compare(this.path.toList, that.path.toList)

  def parent: Standalone = Standalone(path.init)

  def isRoot: Boolean = path.isEmpty

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

case class Cycle(parts: Set[Standalone]) extends Module {
  require(parts.tail.forall(part => parts.head.parent == part.parent))

  override def parent: Standalone = parts.head.parent

  override def toString: String = {
    s"""${parts.toSeq.sorted.mkString("-")}"""
  }
}
