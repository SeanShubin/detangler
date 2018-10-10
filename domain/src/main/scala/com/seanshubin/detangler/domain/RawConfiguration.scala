package com.seanshubin.detangler.domain

import java.nio.file.{Path, Paths}

case class RawConfiguration(reportDir: Option[Path],
                            searchPaths: Option[Seq[Path]],
                            level: Option[Int],
                            startsWith: Option[RawStartsWithConfiguration],
                            ignoreFiles: Option[Seq[Path]],
                            canFailBuild: Option[Boolean],
                            ignoreJavadoc: Option[Boolean],
                            logTiming: Option[Boolean],
                            logEffectiveConfiguration: Option[Boolean],
                            allowedInCycle: Option[Path]) {
  def replaceEmptyWithDefaults(): Configuration = {
    val newReportDir = reportDir.getOrElse(Configuration.Default.reportDir)
    val newSearchPaths = searchPaths.getOrElse(Configuration.Default.searchPaths)
    val newLevel = level.getOrElse(Configuration.Default.level)
    val newStartsWith = startsWith match {
      case None => Configuration.Default.startsWith
      case Some(rawStartsWithConfiguration) => rawStartsWithConfiguration.replaceEmptyWithDefaults()
    }
    val newAllowedInCycle = allowedInCycle.getOrElse(Configuration.Default.allowedInCycle)
    val newIgnoreFiles = ignoreFiles.getOrElse(Configuration.Default.ignoreFiles)
    val newCanFailBuild = canFailBuild.getOrElse(Configuration.Default.canFailBuild)
    val newIgnoreJavadoc = ignoreJavadoc.getOrElse(Configuration.Default.ignoreJavadoc)
    val newLogTiming = logTiming.getOrElse(Configuration.Default.logTiming)
    val newLogEffectiveConfiguration = logEffectiveConfiguration.getOrElse(Configuration.Default.logEffectiveConfiguration)
    Configuration(
      newReportDir,
      newSearchPaths,
      newLevel,
      newStartsWith,
      newIgnoreFiles,
      newCanFailBuild,
      newIgnoreJavadoc,
      newLogTiming,
      newLogEffectiveConfiguration,
      newAllowedInCycle)
  }
}
