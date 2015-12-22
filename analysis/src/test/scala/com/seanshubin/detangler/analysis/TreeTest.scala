package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class TreeTest extends FunSuite {
  test("single item") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val modules = Seq(groupA)
    val expected =
      Tree(root, List(
        Tree(groupA, Nil)))
    val actual = modules.foldLeft(Tree.Empty)(Tree.addStandalone)
    assert(actual === expected)
  }

  test("two items") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val groupB = Standalone(Seq("group/b"))
    val modules = Seq(groupA, groupB)
    val expected =
      Tree(root, List(
        Tree(groupB, Nil),
        Tree(groupA, Nil)))
    val actual = modules.foldLeft(Tree.Empty)(Tree.addStandalone)
    assert(actual === expected)
  }

  test("another level deep") {
    val root = Standalone(Seq())
    val groupA = Standalone(Seq("group/a"))
    val packageC = Standalone(Seq("group/a", "package/c"))
    val modules = Seq(packageC)
    val expected =
      Tree(root, List(
        Tree(groupA, List(
          Tree(packageC, Nil)))))
    val actual = modules.foldLeft(Tree.Empty)(Tree.addStandalone)
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
      Tree(root, List(
        Tree(groupB, List(
          Tree(packageE, List(
            Tree(classI, Nil))))),
        Tree(groupA, List(
          Tree(packageD, List(
            Tree(classH, Nil))),
          Tree(packageC, List(
            Tree(classG, Nil),
            Tree(classF, Nil)))))))
    val actual = modules.foldLeft(Tree.Empty)(Tree.addStandalone)
    assert(actual === expected)
  }
}
