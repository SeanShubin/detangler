package com.seanshubin.detangler.model

object SampleDataWithCycles {
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
  val cycleAB = Cycle(Set(groupA, groupB))
  val cycleCD = Cycle(Set(packageC, packageD))
  val cycleFG = Cycle(Set(classF, classG))
  private val map: Map[Module, ModuleInfo] = Map(
    root -> ModuleInfo(
      id = root,
      children = Set(groupA, groupB, cycleAB)
    ),
    groupA -> ModuleInfo(
      id = groupA,
      children = Set(packageC, packageD, cycleCD),
      dependsOn = Set(groupB),
      dependedOnBy = Set(groupB),
      depth = 1,
      complexity = 1
    ),
    groupB -> ModuleInfo(
      id = groupB,
      children = Set(packageE),
      dependsOn = Set(groupA),
      dependedOnBy = Set(groupA)
    ),
    packageC -> ModuleInfo(
      id = packageC,
      children = Set(classF, classG, cycleFG),
      dependsOn = Set(packageD, packageE),
      dependedOnBy = Set(packageD, packageE),
      depth = 1,
      complexity = 1
    ),
    packageD -> ModuleInfo(
      id = packageD,
      children = Set(classH),
      dependsOn = Set(packageC),
      dependedOnBy = Set(packageC)
    ),
    packageE -> ModuleInfo(
      id = packageE,
      children = Set(classI),
      dependsOn = Set(packageC),
      dependedOnBy = Set(packageC)
    ),
    classF -> ModuleInfo(
      id = classF,
      dependsOn = Set(classG, classH, classI),
      dependedOnBy = Set(classG, classH, classI)
    ),
    classG -> ModuleInfo(
      id = classG,
      dependsOn = Set(classF),
      dependedOnBy = Set(classF)
    ),
    classH -> ModuleInfo(
      id = classH,
      dependsOn = Set(classF),
      dependedOnBy = Set(classF)
    ),
    classI -> ModuleInfo(
      id = classI,
      dependsOn = Set(classF),
      dependedOnBy = Set(classF)
    ),
    cycleAB -> ModuleInfo(
      id = cycleAB,
      parts = Set(groupA, groupB)
    ),
    cycleCD -> ModuleInfo(
      id = cycleCD,
      parts = Set(packageC, packageD)
    ),
    cycleFG -> ModuleInfo(
      id = cycleFG,
      parts = Set(classF, classG)
    )
  )
  val detangled = new DetangledFake(root, map, 3)
}
