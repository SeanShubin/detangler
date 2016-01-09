package com.seanshubin.detangler.core

import java.nio.file.Path
import java.time.Duration

trait Notifications {
  def effectiveConfiguration(configuration: Configuration)

  def configurationError(lines: Seq[String])

  def topLevelException(exception: Throwable)

  def startTiming(caption: String)

  def endTiming(caption: String, duration: Duration)

  def warnNoRelevantClassesInPath(path: Path)
}
