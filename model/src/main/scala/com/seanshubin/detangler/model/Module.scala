package com.seanshubin.detangler.model

import scala.annotation.tailrec

sealed trait Module

case class Standalone(path: Seq[String]) extends Module with Ordered[Standalone] {
  override def toString: String = s"Standalone(${path.mkString("-")})"

  override def compare(that: Standalone): Int = compare(this.path.toList, that.path.toList)

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
  override def toString: String = {
    s"Cycle(${parts.toSeq.sorted.mkString("--")})"
  }
}
