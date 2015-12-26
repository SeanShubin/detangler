package com.seanshubin.detangler.model

import scala.annotation.tailrec

sealed trait Module {
  def isRoot: Boolean

  def parent: Standalone

  def toString: String

  def typeOrder: Int
}

object Module {
  def lessThan(leftModule: Module, rightModule: Module): Boolean = {
    (leftModule, rightModule) match {
      case (leftStandalone: Standalone, rightStandalone: Standalone) =>
        Standalone.lessThan(leftStandalone, rightStandalone)
      case (leftCycle: Cycle, rightCycle: Cycle) =>
        Cycle.lessThan(leftCycle, rightCycle)
      case _ =>
        leftModule.typeOrder < rightModule.typeOrder
    }
  }
}

case class Standalone(path: Seq[String]) extends Module {
  override def toString: String = s"${path.mkString("--")}"

  override def typeOrder: Int = 2

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

  def lessThan(left: Standalone, right: Standalone): Boolean = {
    lessThan(left.path, right.path)
  }

  def lessThan(left: Seq[String], right: Seq[String]): Boolean = {
    (left, right) match {
      case (Nil, Nil) => false
      case (Nil, rightHead :: rightTail) => true
      case (leftHead :: leftTail, Nil) => false
      case (leftHead :: leftTail, rightHead :: rightTail) =>
        if (leftHead == rightHead) {
          lessThan(leftTail, rightTail)
        } else {
          leftHead < rightHead
        }
    }
  }
}

case class Cycle(parts: Set[Standalone]) extends Module {
  require(parts.tail.forall(part => parts.head.parent == part.parent))

  override def typeOrder: Int = 1

  override def isRoot: Boolean = false

  override def parent: Standalone = parts.head.parent

  override def toString: String = {
    s"""${parts.toSeq.sortWith(Standalone.lessThan).mkString("-")}"""
  }
}

object Cycle {
  def lessThan(left: Cycle, right: Cycle): Boolean = {
    lessThan(left.parts.toSeq.sortWith(Standalone.lessThan), right.parts.toSeq.sortWith(Standalone.lessThan))
  }

  def lessThan(left: Seq[Standalone], right: Seq[Standalone]): Boolean = {
    if (left.isEmpty && right.isEmpty) {
      false
    } else {
      if (left.head == right.head) {
        lessThan(left.tail, right.tail)
      } else {
        Standalone.lessThan(left.head, right.head)
      }
    }
  }
}
