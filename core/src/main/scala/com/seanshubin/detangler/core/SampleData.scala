package com.seanshubin.detangler.core

//move back to test package once we start using real data
object SampleData {
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

  val detangledMap: Map[Module, ModuleInfo] = Map(
    idRoot -> ModuleInfo(
      idRoot,
      dependsOn = Set(),
      dependedOnBy = Set(),
      children = Set(idGroupA, idGroupB),
      cycleParts = Set(),
      depth = 0,
      complexity = 0),
    idGroupA -> ModuleInfo(
      idGroupA,
      dependsOn = Set(idGroupB),
      dependedOnBy = Set(),
      children = Set(idPackageC, idPackageD),
      cycleParts = Set(),
      depth = 1,
      complexity = 2),
    idGroupB -> ModuleInfo(
      idGroupB,
      dependsOn = Set(),
      dependedOnBy = Set(idGroupA),
      children = Set(idPackageE),
      cycleParts = Set(),
      depth = 3,
      complexity = 4),
    idPackageC -> ModuleInfo(
      idPackageC,
      dependsOn = Set(idPackageD, idPackageE),
      dependedOnBy = Set(),
      children = Set(idClassF, idClassG),
      cycleParts = Set(),
      depth = 5,
      complexity = 6),
    idPackageD -> ModuleInfo(
      idPackageD,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageC),
      children = Set(idClassH),
      cycleParts = Set(),
      depth = 7,
      complexity = 8),
    idPackageE -> ModuleInfo(
      idPackageE,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageC),
      children = Set(idClassI),
      cycleParts = Set(),
      depth = 9,
      complexity = 10),
    idClassF -> ModuleInfo(
      idClassF,
      dependsOn = Set(idClassG, idClassH, idClassI),
      dependedOnBy = Set(),
      children = Set(),
      cycleParts = Set(),
      depth = 11,
      complexity = 12),
    idClassG -> ModuleInfo(
      idClassG,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      children = Set(),
      cycleParts = Set(),
      depth = 13,
      complexity = 14),
    idClassH -> ModuleInfo(
      idClassH,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      children = Set(),
      cycleParts = Set(),
      depth = 15,
      complexity = 16),
    idClassI -> ModuleInfo(
      idClassI,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      children = Set(),
      cycleParts = Set(),
      depth = 17,
      complexity = 18)
  )
  val detangled: Detangled = DetangledImpl(detangledMap)
}
