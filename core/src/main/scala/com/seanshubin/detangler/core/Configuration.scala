package com.seanshubin.detangler.core

import java.nio.file.{Path, Paths}

case class Limits(cycleCount: Option[Int], cycleSize: Option[Int]) {
  def replaceNullsWithDefaults(): Limits = {
    val newCycleCount = cycleCount match {
      case Some(_) => cycleCount
      case None => Limits.Default.cycleCount
    }
    val newCycleSize = cycleSize match {
      case Some(_) => cycleSize
      case None => Limits.Default.cycleSize
    }
    Limits(newCycleCount, newCycleSize)
  }
}

object Limits {
  val Default = Limits(cycleCount = Some(0), cycleSize = Some(0))
  val Sample = Limits(cycleCount = Some(1), cycleSize = Some(2))
}

case class LimitConfiguration(globalLimits: Limits, moduleLimits: Map[Seq[String], Limits]) {
  def replaceNullsWithDefaults(): LimitConfiguration = {
    val newGlobalLimits = Option(globalLimits).getOrElse(LimitConfiguration.Default.globalLimits)
    val newModuleLimits = Option(moduleLimits).getOrElse(LimitConfiguration.Default.moduleLimits)
    LimitConfiguration(newGlobalLimits, newModuleLimits)
  }
}

object LimitConfiguration {
  val Default = LimitConfiguration(
    globalLimits = Limits.Default,
    moduleLimits = Map())
  val Sample = LimitConfiguration(
    globalLimits = Limits.Default,
    moduleLimits = Map(
      Seq("com", "seanshubin", "cycles") -> Limits.Sample))
}

case class StartsWithConfiguration(include: Seq[Seq[String]], exclude: Seq[Seq[String]], drop: Seq[Seq[String]]) {
  def replaceNullsWithDefaults(): StartsWithConfiguration = {
    val newInclude = Option(include).getOrElse(StartsWithConfiguration.Default.include)
    val newExclude = Option(exclude).getOrElse(StartsWithConfiguration.Default.exclude)
    val newDrop = Option(drop).getOrElse(StartsWithConfiguration.Default.drop)
    StartsWithConfiguration(newInclude, newExclude, newDrop)
  }
}

object StartsWithConfiguration {
  val Default = StartsWithConfiguration(
    include = Seq(),
    exclude = Seq(),
    drop = Seq()
  )
  val Sample = StartsWithConfiguration(
    include = Seq(Seq("com", "seanshubin"), Seq("seanshubin")),
    exclude = Seq(Seq("com", "seanshubin", "unchecked")),
    drop = Seq(Seq("com", "seanshubin"), Seq("seanshubin"))
  )
}

case class Configuration(reportDir: Path,
                         searchPaths: Seq[Path],
                         level: Option[Int],
                         startsWith: StartsWithConfiguration,
                         limits: LimitConfiguration) {
  def replaceNullsWithDefaults(): Configuration = {
    val newReportDir = Option(reportDir).getOrElse(Configuration.Default.reportDir)
    val newSearchPaths = Option(searchPaths).getOrElse(Configuration.Default.searchPaths)
    val newLevel = level match {
      case Some(_) => level
      case None => Configuration.Default.level
    }
    val newStartsWith = Option(startsWith).getOrElse(Configuration.Default.startsWith).replaceNullsWithDefaults()
    val newLimits = Option(limits).getOrElse(Configuration.Default.limits).replaceNullsWithDefaults()
    Configuration(newReportDir, newSearchPaths, newLevel, newStartsWith, newLimits)
  }
}

object Configuration {
  val Default = Configuration(
    reportDir = Paths.get("generated", "detangled"),
    searchPaths = Seq(Paths.get(".")),
    level = Some(1),
    startsWith = StartsWithConfiguration.Default,
    limits = LimitConfiguration.Default
  )

  val Sample = Configuration(
    reportDir = Paths.get("report-dir"),
    searchPaths = Seq(Paths.get("search-path-1"), Paths.get("search-path-2")),
    level = Some(3),
    startsWith = StartsWithConfiguration.Sample,
    limits = LimitConfiguration.Sample)
}
