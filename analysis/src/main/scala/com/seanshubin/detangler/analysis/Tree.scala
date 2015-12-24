package com.seanshubin.detangler.analysis

case class Tree[T](value: T, path: Seq[String] = Seq(), branches: Seq[Tree[T]] = Seq()) {

  import Tree._

  def add(pathToAdd: Seq[String]): Tree[T] = {
    if (path == pathToAdd) {
      this
    } else if (isSuperPath(path, pathToAdd)) {
      val immediateBranchKeys = branches.map(_.path)
      val branchToAdd = pathToAdd.take(path.size + 1)
      val index = immediateBranchKeys.indexOf(branchToAdd)
      if (index == -1) {
        val newEmptyBranch = new Tree(value, branchToAdd)
        val newBranch = newEmptyBranch.add(pathToAdd)
        val newBranches = branches :+ newBranch
        copy(branches = newBranches)
      } else {
        val existingBranch = branches(index)
        val newBranch = existingBranch.add(pathToAdd)
        val newBranches = branches.updated(index, newBranch)
        copy(branches = newBranches)
      }
    } else {
      throw new RuntimeException(s"$path is not a super path of $pathToAdd")
    }
  }

  def keys(): Seq[Seq[String]] = {
    path +: branches.flatMap(_.keys())
  }

  def mapOverTree[U](f: (T, Seq[String]) => U): Tree[U] = {
    new Tree[U](f(value, path), path, branches.par.map(_.mapOverTree(f)).seq)
  }

  def value(valuePath: Seq[String]): T = {
    if (path == valuePath) {
      value
    } else if (isSuperPath(path, valuePath)) {
      val immediateBranchKeys = branches.map(_.path)
      val nextChildDown = valuePath.take(path.size + 1)
      val index = immediateBranchKeys.indexOf(nextChildDown)
      if (index == -1) {
        throw new RuntimeException(s"No value found at $valuePath")
      } else {
        branches(index).value(valuePath)
      }
    } else {
      throw new RuntimeException(s"$path is not a super path of $valuePath")
    }
  }
}

object Tree {
  val Empty = new Tree[Unit](Unit)

  def isSuperPath(left: Seq[String], right: Seq[String]): Boolean = {
    left == right.take(left.size)
  }

  def add(tree: Tree[Unit], path: Seq[String]): Tree[Unit] = tree.add(path)
}
