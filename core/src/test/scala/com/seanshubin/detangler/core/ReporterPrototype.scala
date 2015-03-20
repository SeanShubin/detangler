package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.devon.core.devon.DefaultDevonMarshaller
import com.seanshubin.utility.filesystem.FileSystemIntegrationImpl

object ReporterPrototype extends App {
  private val idGroupA = UnitId.simple("group/a")
  private val idGroupB = UnitId.simple("group/b")
  private val idPackageA = UnitId.simple("group/a", "package/a")
  private val idPackageB = UnitId.simple("group/a", "package/b")
  private val idPackageC = UnitId.simple("group/b", "package/c")
  private val idClassA = UnitId.simple("group/a", "package/a", "class/a")
  private val idClassB = UnitId.simple("group/a", "package/a", "class/b")
  private val idClassC = UnitId.simple("group/a", "package/b", "class/c")
  private val idClassD = UnitId.simple("group/b", "package/c", "class/d")
  private val detangled = Detangled(Map(
    idGroupA -> UnitInfo(
      idGroupA,
      dependsOn = Set(idGroupB),
      dependedOnBy = Set(),
      composedOf = Set(idPackageA, idPackageB),
      depth = 1,
      complexity = 2),
    idGroupB -> UnitInfo(
      idGroupB,
      dependsOn = Set(),
      dependedOnBy = Set(idGroupA),
      composedOf = Set(idPackageC),
      depth = 3,
      complexity = 4),
    idPackageA -> UnitInfo(
      idPackageA,
      dependsOn = Set(idPackageB, idPackageC),
      dependedOnBy = Set(),
      composedOf = Set(idClassA, idClassB),
      depth = 5,
      complexity = 6),
    idPackageB -> UnitInfo(
      idPackageB,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageA),
      composedOf = Set(idClassC),
      depth = 7,
      complexity = 8),
    idPackageC -> UnitInfo(
      idPackageC,
      dependsOn = Set(),
      dependedOnBy = Set(idPackageA),
      composedOf = Set(idClassD),
      depth = 9,
      complexity = 10),
    idClassA -> UnitInfo(
      idClassA,
      dependsOn = Set(idClassB, idClassC, idClassD),
      dependedOnBy = Set(),
      composedOf = Set(),
      depth = 11,
      complexity = 12),
    idClassB -> UnitInfo(
      idClassB,
      dependsOn = Set(),
      dependedOnBy = Set(idClassA),
      composedOf = Set(),
      depth = 13,
      complexity = 14),
    idClassC -> UnitInfo(
      idClassC,
      dependsOn = Set(),
      dependedOnBy = Set(idClassA),
      composedOf = Set(),
      depth = 15,
      complexity = 16),
    idClassD -> UnitInfo(
      idClassD,
      dependsOn = Set(),
      dependedOnBy = Set(idClassA),
      composedOf = Set(),
      depth = 17,
      complexity = 18)
  ))

  val reporter = new ReporterImpl(
    reportDir = Paths.get("generated", "reports"),
    fileSystem = new FileSystemIntegrationImpl,
    devonMarshaller = DefaultDevonMarshaller,
    StandardCharsets.UTF_8
  )

  reporter.generateReports(detangled)
}
