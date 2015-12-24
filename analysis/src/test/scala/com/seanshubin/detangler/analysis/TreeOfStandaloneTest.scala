package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class TreeOfStandaloneTest extends FunSuite {
  test("single item") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val modules = Seq(groupA)
    val expected =
      TreeOfStandalone(root, List(
        TreeOfStandalone(groupA, Nil)))
    val actual = modules.foldLeft(TreeOfStandalone.Empty)(TreeOfStandalone.addStandalone)
    assert(actual === expected)
  }

  test("two items") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val groupB = Standalone(Seq("group/b"))
    val modules = Seq(groupA, groupB)
    val expected =
      TreeOfStandalone(root, List(
        TreeOfStandalone(groupB, Nil),
        TreeOfStandalone(groupA, Nil)))
    val actual = modules.foldLeft(TreeOfStandalone.Empty)(TreeOfStandalone.addStandalone)
    assert(actual === expected)
  }

  test("another level deep") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val packageC = Standalone(Seq("group/a", "package/c"))
    val modules = Seq(packageC)
    val expected =
      TreeOfStandalone(root, List(
        TreeOfStandalone(groupA, List(
          TreeOfStandalone(packageC, Nil)))))
    val actual = modules.foldLeft(TreeOfStandalone.Empty)(TreeOfStandalone.addStandalone)
    assert(actual === expected)
  }

  test("create tree") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val groupB = Standalone(Seq("group/b"))
    val packageC = Standalone(Seq("group/a", "package/c"))
    val packageD = Standalone(Seq("group/a", "package/d"))
    val packageE = Standalone(Seq("group/b", "package/e"))
    val classF = Standalone(Seq("group/a", "package/c", "class/f"))
    val classG = Standalone(Seq("group/a", "package/c", "class/g"))
    val classH = Standalone(Seq("group/a", "package/d", "class/h"))
    val classI = Standalone(Seq("group/b", "package/e", "class/i"))
    val modules = Seq(classF, classG, classH, classI)
    val expected =
      TreeOfStandalone(root, List(
        TreeOfStandalone(groupB, List(
          TreeOfStandalone(packageE, List(
            TreeOfStandalone(classI, Nil))))),
        TreeOfStandalone(groupA, List(
          TreeOfStandalone(packageD, List(
            TreeOfStandalone(classH, Nil))),
          TreeOfStandalone(packageC, List(
            TreeOfStandalone(classG, Nil),
            TreeOfStandalone(classF, Nil)))))))
    val actual = modules.foldLeft(TreeOfStandalone.Empty)(TreeOfStandalone.addStandalone)
    assert(actual === expected)
  }
}
