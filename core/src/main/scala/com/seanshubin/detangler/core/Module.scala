package com.seanshubin.detangler.core

case class Module(paths: Seq[Set[String]]) extends Ordered[Module] {
  override def compare(that: Module): Int = {
    compareSeqSetString(this.paths, that.paths)
  }

  def isCycle: Boolean = {
    def isCycle(part: Set[String]) = part.size > 1
    val result = paths.exists(isCycle)
    result
  }

  def isRoot: Boolean = paths.isEmpty

  def parent: Module = Module(paths.init)

  def qualifiedName: String = {
    def partAsString(part: Set[String]) = part.toSeq.sorted.mkString("-")
    paths.map(partAsString).mkString("--")
  }

  def name: String = {
    paths.last.mkString("-")
  }

  def fileSystemName: String = {
    val safePaths = paths.map(_.map(_.map(Module.makeFileSystemSafe)))
    safePaths.last.mkString("-")
  }

  def anchor: String = {
    if (paths.size == 1) {
      "#" + name
    } else {
      val safePaths = paths.map(_.map(_.map(Module.makeFileSystemSafe)))
      safePaths.init.map(_.mkString("-")).mkString("/") + "#" + name
    }
  }

  private def compareSeqSetString(left: Seq[Set[String]], right: Seq[Set[String]]): Int = {
    if (left.size != right.size) throw new RuntimeException(s"left has size ${left.size}, right has size ${right.size}")
    if (left.isEmpty && right.isEmpty) {
      0
    } else {
      val headCompare = compareSetString(left.head, right.head)
      if (headCompare == 0) {
        compareSeqSetString(left.tail, right.tail)
      } else {
        headCompare
      }
    }
  }

  private def compareSeqString(left: Seq[String], right: Seq[String]): Int = {
    if (left.size != right.size) {
      right.size.compareTo(left.size)
    } else {
      if (left.isEmpty && right.isEmpty) {
        0
      } else {
        val headCompare = left.head.compareTo(right.head)
        if (headCompare == 0) {
          compareSeqString(left.tail, right.tail)
        } else {
          headCompare
        }
      }
    }
  }

  private def compareSetString(left: Set[String], right: Set[String]): Int = {
    compareSeqString(left.toSeq.sorted, right.toSeq.sorted)
  }
}

object Module {
  val Root = simple()

  def simple(ids: String*) = Module(ids.map(Set(_)))

  def complex(parts: Set[String]*) = Module(parts)

  val FileSystemCharacters = "/\\?%*:|\"<>. "

  def makeFileSystemSafe(c: Char): Char = {
    if (FileSystemCharacters.contains(c)) '_'
    else c
  }
}