package com.seanshubin.detangler.core

//move back to test package once we start using real data
object SampleDataWithCycles {
  val idRoot = Module.simple()
  val idGroupA = Module.simple("group/a")
  val idGroupB = Module.simple("group/b")
  val idPackageC = Module.simple("group/a", "package/c")
  val idPackageD = Module.simple("group/a", "package/d")
  val idPackageE = Module.simple("group/b", "package/e")
  val idClassF = Module.simple("group/a", "package/c", "class/f")
  val idClassG = Module.simple("group/a", "package/c", "class/g")
  val idClassH = Module.simple("group/a", "package/d", "class/h")
  val idClassI = Module.simple("group/b", "package/e", "class/i")
  val cycleAB = Module.complex(Set("group/a", "group/b"))
  val cycleCD = Module.complex(Set("group/a"), Set("package/c", "package/d"))
  val cycleFG = Module.complex(Set("group/a"), Set("package/c"), Set("class/f", "class/g"))

  val detangledMap: Map[Module, ModuleInfo] = Map(
    idRoot -> ModuleInfo(
      idRoot,
      dependsOn = Set(),
      dependedOnBy = Set(),
      children = Set(cycleAB, idGroupA, idGroupB),
      cycleParts = Set(),
      depth = 0,
      complexity = 0),
    idGroupA -> ModuleInfo(
      idGroupA,
      dependsOn = Set(idGroupB),
      dependedOnBy = Set(idGroupB),
      children = Set(cycleCD, idPackageC, idPackageD),
      cycleParts = Set(),
      depth = 1,
      complexity = 2),
    idGroupB -> ModuleInfo(
      idGroupB,
      dependsOn = Set(idGroupA),
      dependedOnBy = Set(idGroupA),
      children = Set(idPackageE),
      cycleParts = Set(),
      depth = 3,
      complexity = 4),
    idPackageC -> ModuleInfo(
      idPackageC,
      dependsOn = Set(idPackageD, idPackageE),
      dependedOnBy = Set(idPackageD, idPackageE),
      children = Set(cycleFG, idClassF, idClassG),
      cycleParts = Set(),
      depth = 5,
      complexity = 6),
    idPackageD -> ModuleInfo(
      idPackageD,
      dependsOn = Set(idPackageC),
      dependedOnBy = Set(idPackageC),
      children = Set(idClassH),
      cycleParts = Set(),
      depth = 7,
      complexity = 8),
    idPackageE -> ModuleInfo(
      idPackageE,
      dependsOn = Set(idPackageC),
      dependedOnBy = Set(idPackageC),
      children = Set(idClassI),
      cycleParts = Set(),
      depth = 9,
      complexity = 10),
    idClassF -> ModuleInfo(
      idClassF,
      dependsOn = Set(idClassG, idClassH, idClassI),
      dependedOnBy = Set(idClassG, idClassH, idClassI),
      children = Set(),
      cycleParts = Set(),
      depth = 11,
      complexity = 12),
    idClassG -> ModuleInfo(
      idClassG,
      dependsOn = Set(idClassF),
      dependedOnBy = Set(idClassF),
      children = Set(),
      cycleParts = Set(),
      depth = 13,
      complexity = 14),
    idClassH -> ModuleInfo(
      idClassH,
      dependsOn = Set(idClassF),
      dependedOnBy = Set(idClassF),
      children = Set(),
      cycleParts = Set(),
      depth = 15,
      complexity = 16),
    idClassI -> ModuleInfo(
      idClassI,
      dependsOn = Set(idClassF),
      dependedOnBy = Set(idClassF),
      children = Set(),
      cycleParts = Set(),
      depth = 17,
      complexity = 18),
    cycleAB -> ModuleInfo(
      id = cycleAB,
      dependsOn = Set(),
      dependedOnBy = Set(),
      children = Set(),
      cycleParts = Set(idGroupA, idGroupB),
      depth = 19,
      complexity = 20),
    cycleCD -> ModuleInfo(
      id = cycleCD,
      dependsOn = Set(),
      dependedOnBy = Set(),
      children = Set(),
      cycleParts = Set(idPackageC, idPackageD),
      depth = 21,
      complexity = 22),
    cycleFG -> ModuleInfo(
      id = cycleFG,
      dependsOn = Set(),
      dependedOnBy = Set(),
      children = Set(),
      cycleParts = Set(idClassF, idClassG),
      depth = 23,
      complexity = 24))
  val detangled: Detangled = DetangledImpl(detangledMap)
}
