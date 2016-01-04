package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.collection.SeqDifference
import org.scalatest.FunSuite

class TreeTest extends FunSuite {
  val root = Seq()
  val groupA = Seq("group/a")
  val groupB = Seq("group/b")
  val packageC = Seq("group/a", "package/c")
  val packageD = Seq("group/a", "package/d")
  val packageE = Seq("group/b", "package/e")
  val classF = Seq("group/a", "package/c", "class/f")
  val classG = Seq("group/a", "package/c", "class/g")
  val classH = Seq("group/a", "package/d", "class/h")
  val classI = Seq("group/b", "package/e", "class/i")

  test("construct tree") {
    val tree = Tree.Empty.add(classF).add(classG).add(classH).add(classI)
    val keys = tree.keys()
    assert(keys.size === 10)
    assert(keys(0) === root)
    assert(keys(1) === groupA)
    assert(keys(2) === packageC)
    assert(keys(3) === classF)
    assert(keys(4) === classG)
    assert(keys(5) === packageD)
    assert(keys(6) === classH)
    assert(keys(7) === groupB)
    assert(keys(8) === packageE)
    assert(keys(9) === classI)
  }

  test("map function over tree") {
    val tree = Tree.Empty.add(classF).add(classG).add(classH).add(classI)
    def composeName(value: Unit, path: Seq[String]): String = {
      def modifyPathPart(pathPart: String): String = {
        pathPart.dropWhile(_ != '/').tail
      }
      path.map(modifyPathPart).mkString("(", "", ")")
    }
    val actual = tree.mapOverTree(composeName)
    assert(actual.value(root) === "()")
    assert(actual.value(groupA) === "(a)")
    assert(actual.value(groupB) === "(b)")
    assert(actual.value(packageC) === "(ac)")
    assert(actual.value(packageD) === "(ad)")
    assert(actual.value(packageE) === "(be)")
    assert(actual.value(classF) === "(acf)")
    assert(actual.value(classG) === "(acg)")
    assert(actual.value(classH) === "(adh)")
    assert(actual.value(classI) === "(bei)")
  }

  test("breadth first") {
    val tree = Tree.Empty.add(classF).add(classG).add(classH).add(classI)
    def composeName(value: Unit, path: Seq[String]): String = {
      def modifyPathPart(pathPart: String): String = {
        pathPart.dropWhile(_ != '/').tail
      }
      path.map(modifyPathPart).mkString("(", "", ")")
    }
    val actual = tree.breadthFirst()
    val expected = Seq(
      root,
      groupA,
      groupB,
      packageC,
      packageD,
      packageE,
      classF,
      classG,
      classH,
      classI)
    val diff = SeqDifference.diff(actual, expected)

  }
}
