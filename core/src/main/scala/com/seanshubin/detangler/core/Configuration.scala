package com.seanshubin.detangler.core

import java.nio.file.{Path, Paths}

case class StartsWithConfiguration(include: Seq[Seq[String]],
                                   exclude: Seq[Seq[String]],
                                   drop: Seq[Seq[String]]) {
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
                         ignoreFiles: Seq[Path],
                         canFailBuild: Option[Boolean],
                         allowedInCycle: Seq[Seq[String]]) {
  def replaceNullsWithDefaults(): Configuration = {
    val newReportDir = Option(reportDir).getOrElse(Configuration.Default.reportDir)
    val newSearchPaths = Option(searchPaths).getOrElse(Configuration.Default.searchPaths)
    val newLevel = level match {
      case Some(_) => level
      case None => Configuration.Default.level
    }
    val newStartsWith = Option(startsWith).getOrElse(Configuration.Default.startsWith).replaceNullsWithDefaults()
    val newAllowedInCycle = Option(allowedInCycle).getOrElse(Configuration.Default.allowedInCycle)
    val newIgnoreFiles = Option(ignoreFiles).getOrElse(Configuration.Default.ignoreFiles)
    val newCanFailBuild = canFailBuild match {
      case Some(_) => canFailBuild
      case None => Configuration.Default.canFailBuild
    }
    Configuration(
      newReportDir,
      newSearchPaths,
      newLevel,
      newStartsWith,
      newIgnoreFiles,
      newCanFailBuild,
      newAllowedInCycle)
  }
}

object Configuration {
  val Default = Configuration(
    reportDir = Paths.get("generated", "detangled"),
    searchPaths = Seq(Paths.get(".")),
    level = Some(2),
    startsWith = StartsWithConfiguration.Default,
    allowedInCycle = Seq(),
    ignoreFiles = Seq(),
    canFailBuild = Some(false)
  )

  val Sample = Configuration(
    reportDir = Paths.get("report-dir"),
    searchPaths = Seq(Paths.get("search-path-1"), Paths.get("search-path-2")),
    level = Some(3),
    startsWith = StartsWithConfiguration.Sample,
    allowedInCycle = Seq(Seq("branch"), Seq("tree"), Seq("leaf")),
    ignoreFiles = Seq(Paths.get("ignore-file.jar")),
    canFailBuild = Some(true))
}
