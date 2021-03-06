package com.seanshubin.detangler.model

import com.seanshubin.detangler.compare.Compare

sealed trait Module {
  def isRoot: Boolean

  def parent: Standalone

  def toString: String

  def typeOrder: Int

  def level: Int

  def standalone: Standalone
}

object Module {
  val compare: (Module, Module) => Int = (leftModule, rightModule) => {
    (leftModule, rightModule) match {
      case (leftStandalone: Standalone, rightStandalone: Standalone) =>
        Standalone.compare(leftStandalone, rightStandalone)
      case (leftCycle: Cycle, rightCycle: Cycle) =>
        Cycle.compare(leftCycle, rightCycle)
      case _ =>
        leftModule.typeOrder.compareTo(rightModule.typeOrder)
    }
  }
}

case class Standalone(path: Seq[String]) extends Module {
  override def toString: String = s"${path.mkString("--")}"

  override def typeOrder: Int = 2

  def parent: Standalone = Standalone(path.init)

  override def isRoot: Boolean = path.isEmpty

  override def level: Int = path.size

  override def standalone: Standalone = this

  def atLevel(targetLevel: Int): Standalone = Standalone(path.take(targetLevel))

  def compareTo(that: Standalone) = Standalone.compare(this, that)
}

object Standalone {
  val Root = Standalone(Seq())
  val compare: (Standalone, Standalone) => Int = (left: Standalone, right: Standalone) => {
    val comparePaths = Compare.composeCompareSeqFunction(Ordering.String.compare)
    comparePaths(left.path.toList, right.path.toList)
  }
}

case class Cycle(parts: Set[Standalone]) extends Module {
  require(parts.tail.forall(part => parts.head.parent == part.parent))

  override def typeOrder: Int = 1

  override def isRoot: Boolean = false

  override def parent: Standalone = parts.head.parent

  override def toString: String = {
    s"""${parts.toSeq.sortWith(lessThan).mkString("-")}"""
  }

  override def level: Int = standalone.level

  def standalone: Standalone = parts.toSeq.sortWith(lessThan).head

  def size: Int = parts.size

  private val lessThan = Compare.lessThan(Standalone.compare)
}

object Cycle {
  val compare: (Cycle, Cycle) => Int = (left, right) => {
    val compareParts = Compare.composeCompareSeqFunction(Standalone.compare)
    val leftParts = sortedParts(left)
    val rightParts = sortedParts(right)
    compareParts(leftParts, rightParts)
  }

  private def sortedParts(cycle: Cycle): List[Standalone] = {
    cycle.parts.toSeq.sortWith(Compare.lessThan(Standalone.compare)).toList
  }
}
