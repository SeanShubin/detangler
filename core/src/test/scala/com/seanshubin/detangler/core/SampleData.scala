package com.seanshubin.detangler.core

object SampleData {
  val idRoot = UnitId.simple()
  val idGroupA = UnitId.simple("group/a")
  val idGroupB = UnitId.simple("group/b")
  val idPackageA = UnitId.simple("group/a", "package/c")
  val idPackageB = UnitId.simple("group/a", "package/d")
  val idPackageC = UnitId.simple("group/b", "package/e")
  val idClassA = UnitId.simple("group/a", "package/c", "class/f")
  val idClassB = UnitId.simple("group/a", "package/c", "class/g")
  val idClassC = UnitId.simple("group/a", "package/d", "class/h")
  val idClassD = UnitId.simple("group/b", "package/e", "class/i")
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
      composedOf = Set(idPackageA, idPackageB),
      depth = 1,
      complexity = 2),
    idGroupB -> UnitInfo(
      idGroupB,
      dependsOn = Set(),
      dependedOnBy = Set(idGroupA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idPackageC),
      depth = 3,
      complexity = 4),
    idPackageA -> UnitInfo(
      idPackageA,
      dependsOn = Set(idPackageB, idPackageC),
      dependedOnBy = Set(),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idClassA, idClassB),
      depth = 5,
      complexity = 6),
    idPackageB -> UnitInfo(
      idPackageB,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idClassC),
      depth = 7,
      complexity = 8),
    idPackageC -> UnitInfo(
      idPackageC,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(idClassD),
      depth = 9,
      complexity = 10),
    idClassA -> UnitInfo(
      idClassA,
      dependsOn = Set(idClassB, idClassC, idClassD),
      dependedOnBy = Set(),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 11,
      complexity = 12),
    idClassB -> UnitInfo(
      idClassB,
      dependsOn = Set(),
      dependedOnBy = Set(idClassA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 13,
      complexity = 14),
    idClassC -> UnitInfo(
      idClassC,
      dependsOn = Set(),
      dependedOnBy = Set(idClassA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 15,
      complexity = 16),
    idClassD -> UnitInfo(
      idClassD,
      dependsOn = Set(),
      dependedOnBy = Set(idClassA),
      dependsOnExternal = Set(),
      dependedOnByExternal = Set(),
      composedOf = Set(),
      depth = 17,
      complexity = 18)
  ))

}
