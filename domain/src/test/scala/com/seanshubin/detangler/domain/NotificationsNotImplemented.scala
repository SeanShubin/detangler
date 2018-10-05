package com.seanshubin.detangler.domain

import java.nio.file.Path
import java.time.Duration

import com.seanshubin.detangler.model.Standalone

class NotificationsNotImplemented extends Notifications {
  override def reportGenerated(indexPath: Path): Unit = ???

  override def effectiveConfiguration(configuration: Configuration): Unit = ???

  override def configurationError(lines: Seq[String]): Unit = ???

  override def topLevelException(exception: Throwable): Unit = ???

  override def startTiming(caption: String): Unit = ???

  override def endTiming(caption: String, duration: Duration): Unit = ???

  override def warnNoRelevantClassesInPath(path: Path): Unit = ???

  override def newCycleParts(cycleParts: Seq[Standalone]): Unit = ???
}
