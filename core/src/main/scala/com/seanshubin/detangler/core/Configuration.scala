package com.seanshubin.detangler.core

import java.nio.file.{Paths, Path}

case class Configuration(reportDir: Path, searchPaths: Seq[Path])

object Configuration {
  val Sample = Configuration(
    reportDir = Paths.get("report-dir"),
    searchPaths = Seq(Paths.get("search-path-1"), Paths.get("search-path-2"))
  )
}
