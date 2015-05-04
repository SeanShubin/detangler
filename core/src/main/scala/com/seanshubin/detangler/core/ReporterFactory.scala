package com.seanshubin.detangler.core

import java.nio.file.Path

trait ReporterFactory {
  def createReporter(detangled: Detangled, reportDir: Path): Reporter
}
