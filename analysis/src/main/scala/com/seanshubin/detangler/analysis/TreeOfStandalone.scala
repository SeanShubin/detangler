package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

case class TreeOfStandalone(parent: Standalone, children: List[TreeOfStandalone]) {
  def addStandalone(standalone: Standalone): TreeOfStandalone = {
    if (parent.level == standalone.level) {
      if (parent == standalone) this
      else throw new RuntimeException(s"$parent did not equal $standalone")
    } else {
      val atChildLevel = standalone.atLevel(parent.level + 1)
      val childNodes = children.map(_.parent)
      val index = childNodes.indexOf(atChildLevel)
      if (index == -1) {
        val emptyChild = TreeOfStandalone(atChildLevel, Nil)
        val newChild = emptyChild.addStandalone(standalone)
        val newChildren = newChild :: children
        copy(children = newChildren)
      } else {
        val existingChild = children(index)
        val newChild = existingChild.addStandalone(standalone)
        val newChildren = children.updated(index, newChild)
        copy(children = newChildren)
      }
    }
  }

  def toMultipleLineString: Seq[String] = {
    toMultipleLineString(0)
  }

  def toMultipleLineString(depth: Int): Seq[String] = {
    val firstLine = "  " * depth + parent.toString
    val remainingLines = children.flatMap(_.toMultipleLineString(depth + 1))
    val lines = firstLine +: remainingLines
    lines
  }
}

object TreeOfStandalone {
  val Empty = TreeOfStandalone(Standalone(Seq()), Nil)

  def addStandalone(tree: TreeOfStandalone, standalone: Standalone): TreeOfStandalone = {
    tree.addStandalone(standalone)
  }
}