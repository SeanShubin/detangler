package com.seanshubin.detangler.domain

import java.nio.file.Path

case class RawConfiguration(reportDir: Option[Path],
                            searchPaths: Option[Seq[Path]],
                            level: Option[Int],
                            startsWith: Option[RawStartsWithConfiguration],
                            ignoreFiles: Option[Seq[Path]],
                            canFailBuild: Option[Boolean],
                            ignoreJavadoc: Option[Boolean],
                            logTiming: Option[Boolean],
                            logEffectiveConfiguration: Option[Boolean],
                            allowedInCycle: Option[Path],
                            pathsRelativeToCurrentDirectory: Option[Boolean],
                            pathsRelativeToConfigurationDirectory: Option[Boolean]) {
  def replaceEmptyWithDefaults(configurationPath: Path): Configuration = {
    val newPathsRelativeToConfigDirectory = !pathsRelativeToCurrentDirectory.getOrElse(false) && pathsRelativeToConfigurationDirectory.getOrElse(false)
    val newPathsRelativeToCurrentDirectory = !newPathsRelativeToConfigDirectory
    def modifyRelativePathIfUsingConfigurationDirectory(path: Path): Path =
      if (newPathsRelativeToConfigDirectory && !path.isAbsolute) configurationPath.getParent.resolve(path)
      else path

    val newReportDir = modifyRelativePathIfUsingConfigurationDirectory(reportDir.getOrElse(Configuration.Default.reportDir))
    val newSearchPaths = searchPaths.getOrElse(Configuration.Default.searchPaths).map(modifyRelativePathIfUsingConfigurationDirectory)
    val newLevel = level.getOrElse(Configuration.Default.level)
    val newStartsWith = startsWith match {
      case None => Configuration.Default.startsWith
      case Some(rawStartsWithConfiguration) => rawStartsWithConfiguration.replaceEmptyWithDefaults()
    }
    val newAllowedInCycle = modifyRelativePathIfUsingConfigurationDirectory(allowedInCycle.getOrElse(Configuration.Default.allowedInCycle))
    val newIgnoreFiles = ignoreFiles.getOrElse(Configuration.Default.ignoreFiles).map(modifyRelativePathIfUsingConfigurationDirectory)
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
      newAllowedInCycle,
      newPathsRelativeToCurrentDirectory,
      newPathsRelativeToConfigDirectory)
  }
}
