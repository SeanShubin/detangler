package com.seanshubin.detangler.core

import java.nio.file.Path
import java.time.Duration

import com.seanshubin.detangler.model.Standalone

trait Notifications {
  def reportGenerated(indexPath: Path): Unit

  def effectiveConfiguration(configuration: Configuration)

  def configurationError(lines: Seq[String])

  def topLevelException(exception: Throwable)

  def startTiming(caption: String)

  def endTiming(caption: String, duration: Duration)

  def warnNoRelevantClassesInPath(path: Path)

  def newCycleParts(cycleParts: Seq[Standalone])
}
