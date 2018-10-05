package com.seanshubin.detangler.domain

import java.nio.file.Path
import java.time.Duration

import com.seanshubin.detangler.model.Standalone

trait Notifications {
  def reportGenerated(indexPath: Path, cycleParts: Seq[Standalone]): Unit

  def effectiveConfiguration(configuration: Configuration)

  def configurationError(lines: Seq[String])

  def topLevelException(exception: Throwable)

  def startTiming(caption: String)

  def endTiming(caption: String, duration: Duration)

  def warnNoRelevantClassesInPath(path: Path)
}
