package com.seanshubin.detangler.core

import java.nio.file.Path
import java.time.Duration

import com.seanshubin.detangler.model.Standalone

import scala.collection.mutable.ArrayBuffer

class NotificationsStub extends Notifications {
  val invocations = new ArrayBuffer[AnyRef]

  override def configurationError(lines: Seq[String]): Unit = {
    invocations.append(("configurationError", lines))
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    invocations.append(("effectiveConfiguration", configuration))
  }

  override def topLevelException(exception: Throwable): Unit = ???

  override def startTiming(caption: String): Unit = ???

  override def endTiming(caption: String, duration: Duration): Unit = ???

  override def warnNoRelevantClassesInPath(path: Path): Unit = ???

  override def newCycleParts(cycleParts: Seq[Standalone]): Unit = ???

  override def reportGenerated(indexPath: Path): Unit = ???
}
