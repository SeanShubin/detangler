package com.seanshubin.detangler.domain

import java.nio.file.{Path, Paths}

case class Configuration(reportDir: Path,
                         searchPaths: Seq[Path],
                         level: Option[Int],
                         startsWith: StartsWithConfiguration,
                         ignoreFiles: Seq[Path],
                         canFailBuild: Option[Boolean],
                         ignoreJavadoc: Option[Boolean],
                         allowedInCycle: Path) {
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
    val newIgnoreJavadoc = ignoreJavadoc match {
      case Some(_) => ignoreJavadoc
      case None => Configuration.Default.ignoreJavadoc
    }
    Configuration(
      newReportDir,
      newSearchPaths,
      newLevel,
      newStartsWith,
      newIgnoreFiles,
      newCanFailBuild,
      newIgnoreJavadoc,
      newAllowedInCycle)
  }
}

object Configuration {
  val Default = Configuration(
    reportDir = Paths.get("target", "detangled"),
    searchPaths = Seq(Paths.get(".")),
    level = Some(2),
    startsWith = StartsWithConfiguration.Default,
    allowedInCycle = Paths.get("detangler-allowed-in-cycle.txt"),
    ignoreFiles = Seq(),
    canFailBuild = Some(false),
    ignoreJavadoc = Some(true)
  )

  val Sample = Configuration(
    reportDir = Paths.get("report-dir"),
    searchPaths = Seq(Paths.get("search-path-1"), Paths.get("search-path-2")),
    level = Some(3),
    startsWith = StartsWithConfiguration.Sample,
    allowedInCycle = Paths.get("detangler-allowed-in-cycle.txt"),
    ignoreFiles = Seq(Paths.get("ignore-file.jar")),
    canFailBuild = Some(true),
    ignoreJavadoc = Some(true))

  val SampleAllowedInCycles = Seq(Seq("branch"), Seq("tree"), Seq("leaf"))
}
