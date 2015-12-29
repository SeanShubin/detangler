package com.seanshubin.detangler.core

import java.nio.file.{Path, Paths}

case class StartsWithConfiguration(include: Seq[Seq[String]], drop: Seq[Seq[String]])

case class Configuration(reportDir: Path, searchPaths: Seq[Path], level: Int, startsWith: StartsWithConfiguration)

object Configuration {
  val Sample = Configuration(
    reportDir = Paths.get("report-dir"),
    searchPaths = Seq(Paths.get("search-path-1"), Paths.get("search-path-2")),
    level = 3,
    startsWith = StartsWithConfiguration(
      include = Seq(Seq("com", "seanshubin"), Seq("seanshubin")),
      drop = Seq(Seq("com", "seanshubin"), Seq("seanshubin"))
    )
  )
}
