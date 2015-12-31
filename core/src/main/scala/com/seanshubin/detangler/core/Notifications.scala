package com.seanshubin.detangler.core

import java.time.Duration

trait Notifications {
  def effectiveConfiguration(configuration: Configuration)

  def configurationError(lines: Seq[String])

  def topLevelException(exception: Throwable)

  def timeTaken(duration: Duration)
}
