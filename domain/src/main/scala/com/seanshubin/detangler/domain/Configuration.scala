package com.seanshubin.detangler.domain

import java.nio.file.{Path, Paths}

case class Configuration(reportDir: Path,
                         searchPaths: Seq[Path],
                         level: Int,
                         startsWith: StartsWithConfiguration,
                         ignoreFiles: Seq[Path],
                         canFailBuild: Boolean,
                         ignoreJavadoc: Boolean,
                         logTiming: Boolean,
                         logEffectiveConfiguration: Boolean,
                         allowedInCycle: Path,
                         pathsRelativeToCurrentDirectory: Boolean,
                         pathsRelativeToConfigurationDirectory:Boolean)


object Configuration {
  val Default = Configuration(
    reportDir = Paths.get("target", "detangled"),
    searchPaths = Seq(Paths.get(".")),
    level = 2,
    startsWith = StartsWithConfiguration.Default,
    allowedInCycle = Paths.get("detangler-allowed-in-cycle.txt"),
    ignoreFiles = Seq(),
    canFailBuild = false,
    ignoreJavadoc = true,
    logTiming = true,
    logEffectiveConfiguration = true,
    pathsRelativeToCurrentDirectory = true,
    pathsRelativeToConfigurationDirectory = false
  )

  val Sample = Configuration(
    reportDir = Paths.get("report-dir"),
    searchPaths = Seq(Paths.get("search-path-1"), Paths.get("search-path-2")),
    level = 3,
    startsWith = StartsWithConfiguration.Sample,
    allowedInCycle = Paths.get("detangler-allowed-in-cycle.txt"),
    ignoreFiles = Seq(Paths.get("ignore-file.jar")),
    canFailBuild = true,
    ignoreJavadoc = true,
    logTiming = true,
    logEffectiveConfiguration = true,
    pathsRelativeToCurrentDirectory = true,
    pathsRelativeToConfigurationDirectory = false)

  val SampleAllowedInCycles = Seq(Seq("branch"), Seq("tree"), Seq("leaf"))
}
