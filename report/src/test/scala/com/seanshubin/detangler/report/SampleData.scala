package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Module, Single}

object SampleData {
  val root = Single(Seq())
  val groupA = Single(Seq("group/a"))
  val groupB = Single(Seq("group/b"))
  val packageC = Single(Seq("group/a", "package/c"))
  val packageD = Single(Seq("group/a", "package/d"))
  val packageE = Single(Seq("group/b", "package/e"))
  val classF = Single(Seq("group/a", "package/c", "class/f"))
  val classG = Single(Seq("group/a", "package/c", "class/g"))
  val classH = Single(Seq("group/a", "package/d", "class/h"))
  val classI = Single(Seq("group/b", "package/e", "class/i"))
  private val map: Map[Module, ModuleInfo] = Map(
    root -> ModuleInfo(
      id = root,
      children = Set(groupA, groupB)
    ),
    groupA -> ModuleInfo(
      id = groupA,
      children = Set(packageC, packageD),
      dependsOn = Set(groupB),
      depth = 1,
      complexity = 1
    ),
    groupB -> ModuleInfo(
      id = groupB,
      children = Set(packageE),
      dependedOnBy = Set(groupA)
    ),
    packageC -> ModuleInfo(
      id = packageC,
      children = Set(classF, classG),
      dependsOn = Set(packageD, packageE),
      depth = 1,
      complexity = 1
    ),
    packageD -> ModuleInfo(
      id = packageD,
      children = Set(classH),
      dependedOnBy = Set(packageC)
    ),
    packageE -> ModuleInfo(
      id = packageE,
      children = Set(classI),
      dependedOnBy = Set(packageC)
    ),
    classF -> ModuleInfo(
      id = classF,
      dependsOn = Set(classG, classH, classI)
    ),
    classG -> ModuleInfo(
      id = classG,
      dependedOnBy = Set(classF)
    ),
    classH -> ModuleInfo(
      id = classH,
      dependedOnBy = Set(classF)
    ),
    classI -> ModuleInfo(
      id = classI,
      dependedOnBy = Set(classF)
    )
  )
  val detangled = new DetangledFake(root, map)
}
