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
      composedOf = Set(idGroupA, idGroupB),
      depth = 0,
      complexity = 0),
    idGroupA -> ModuleInfo(
      idGroupA,
      dependsOn = Set(idGroupB),
      dependedOnBy = Set(),
      composedOf = Set(idPackageC, idPackageD),
      depth = 1,
      complexity = 2),
    idGroupB -> ModuleInfo(
      idGroupB,
      dependsOn = Set(),
      dependedOnBy = Set(idGroupA),
      composedOf = Set(idPackageE),
      depth = 3,
      complexity = 4),
    idPackageC -> ModuleInfo(
      idPackageC,
      dependsOn = Set(idPackageD, idPackageE),
      dependedOnBy = Set(),
      composedOf = Set(idClassF, idClassG),
      depth = 5,
      complexity = 6),
    idPackageD -> ModuleInfo(
      idPackageD,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageC),
      composedOf = Set(idClassH),
      depth = 7,
      complexity = 8),
    idPackageE -> ModuleInfo(
      idPackageE,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageC),
      composedOf = Set(idClassI),
      depth = 9,
      complexity = 10),
    idClassF -> ModuleInfo(
      idClassF,
      dependsOn = Set(idClassG, idClassH, idClassI),
      dependedOnBy = Set(),
      composedOf = Set(),
      depth = 11,
      complexity = 12),
    idClassG -> ModuleInfo(
      idClassG,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      composedOf = Set(),
      depth = 13,
      complexity = 14),
    idClassH -> ModuleInfo(
      idClassH,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      composedOf = Set(),
      depth = 15,
      complexity = 16),
    idClassI -> ModuleInfo(
      idClassI,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      composedOf = Set(),
      depth = 17,
      complexity = 18)
  )
  val detangled: Detangled = DetangledImpl(detangledMap)
}
