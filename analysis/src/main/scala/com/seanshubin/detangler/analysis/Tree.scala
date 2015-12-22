package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

case class Tree(parent: Standalone, children: List[Tree]) {
  def addStandalone(standalone: Standalone): Tree = {
    if (parent.level == standalone.level) {
      if (parent == standalone) this
      else throw new RuntimeException(s"$parent did not equal $standalone")
    } else {
      val atChildLevel = standalone.atLevel(parent.level + 1)
      val childNodes = children.map(_.parent)
      val index = childNodes.indexOf(atChildLevel)
      if (index == -1) {
        val emptyChild = Tree(atChildLevel, Nil)
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

object Tree {
  val Empty = Tree(Standalone(Seq()), Nil)

  def addStandalone(tree: Tree, standalone: Standalone): Tree = {
    tree.addStandalone(standalone)
  }
}