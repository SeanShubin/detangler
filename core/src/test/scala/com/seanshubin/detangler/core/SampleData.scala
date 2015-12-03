package com.seanshubin.detangler.core

object SampleData {
  val idRoot = UnitId.simple()
  val idGroupA = UnitId.simple("group/a")
  val idGroupB = UnitId.simple("group/b")
  val idPackageC = UnitId.simple("group/a", "package/c")
  val idPackageD = UnitId.simple("group/a", "package/d")
  val idPackageE = UnitId.simple("group/b", "package/e")
  val idClassF = UnitId.simple("group/a", "package/c", "class/f")
  val idClassG = UnitId.simple("group/a", "package/c", "class/g")
  val idClassH = UnitId.simple("group/a", "package/d", "class/h")
  val idClassI = UnitId.simple("group/b", "package/e", "class/i")
  val detangled: Detangled = DetangledImpl(Map(
    idRoot -> UnitInfo(
      idRoot,
      dependsOn = Set(),
      dependedOnBy = Set(),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idGroupA, idGroupB),
      depth = 0,
      complexity = 0),
    idGroupA -> UnitInfo(
      idGroupA,
      dependsOn = Set(idGroupB),
      dependedOnBy = Set(),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idPackageC, idPackageD),
      depth = 1,
      complexity = 2),
    idGroupB -> UnitInfo(
      idGroupB,
      dependsOn = Set(),
      dependedOnBy = Set(idGroupA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idPackageE),
      depth = 3,
      complexity = 4),
    idPackageC -> UnitInfo(
      idPackageC,
      dependsOn = Set(idPackageD, idPackageE),
      dependedOnBy = Set(),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idClassF, idClassG),
      depth = 5,
      complexity = 6),
    idPackageD -> UnitInfo(
      idPackageD,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageC),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idClassH),
      depth = 7,
      complexity = 8),
    idPackageE -> UnitInfo(
      idPackageE,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageC),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idClassI),
      depth = 9,
      complexity = 10),
    idClassF -> UnitInfo(
      idClassF,
      dependsOn = Set(idClassG, idClassH, idClassI),
      dependedOnBy = Set(),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 11,
      complexity = 12),
    idClassG -> UnitInfo(
      idClassG,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 13,
      complexity = 14),
    idClassH -> UnitInfo(
      idClassH,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 15,
      complexity = 16),
    idClassI -> UnitInfo(
      idClassI,
      dependsOn = Set(),
      dependedOnBy = Set(idClassF),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 17,
      complexity = 18)
  ))

}
