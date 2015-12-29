package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.data.DependencyAccumulator
import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class DependencyDataTest extends FunSuite {
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

  val accumulator = DependencyAccumulator.empty[Standalone]().addValues(classF, Seq(classG, classH, classI))
  val dependencyData = DependencyData.fromMaps(accumulator.dependencies, accumulator.transpose().dependencies)

  test("root subset") {
    val subset = dependencyData.subsetFor(root.path)
    assert(subset.level === 1)
    assert(subset.all === Set(groupA, groupB))
    assert(subset.dependsOn === Map(groupA -> Set(groupB), groupB -> Set()))
    assert(subset.dependedOnBy === Map(groupA -> Set(), groupB -> Set(groupA)))
  }

  test("group subset") {
    val subset = dependencyData.subsetFor(groupA.path)
    assert(subset.level === 2)
    assert(subset.all === Set(packageC, packageD))
    assert(subset.dependsOn === Map(packageC -> Set(packageD), packageD -> Set()))
    assert(subset.dependedOnBy === Map(packageC -> Set(), packageD -> Set(packageC)))
  }

  test("package subset") {
    val subset = dependencyData.subsetFor(packageC.path)
    assert(subset.level === 3)
    assert(subset.all === Set(classF, classG))
    assert(subset.dependsOn === Map(classF -> Set(classG), classG -> Set()))
    assert(subset.dependedOnBy === Map(classF -> Set(), classG -> Set(classF)))
  }

  test("class subset") {
    val subset = dependencyData.subsetFor(classF.path)
    assert(subset.level === 4)
    assert(subset.all === Set())
    assert(subset.dependsOn === Map())
    assert(subset.dependedOnBy === Map())
  }

  test("higher level") {
    val packageLevel = dependencyData.higherLevel()
    val groupLevel = packageLevel.higherLevel()
    assert(groupLevel.level === 1)
    assert(groupLevel.all === Set(groupA, groupB))
    assert(groupLevel.dependsOn === Map(groupA -> Set(groupB), groupB -> Set()))
    assert(groupLevel.dependedOnBy === Map(groupA -> Set(), groupB -> Set(groupA)))
  }
}
