package com.seanshubin.detangler.core

import java.io.{PrintWriter, StringWriter}
import java.nio.file.Path
import java.time.Duration

import com.seanshubin.devon.core.devon.DevonMarshaller

class LineEmittingNotifications(devonMarshaller: DevonMarshaller,
                                emit: String => Unit) extends Notifications {
  private val timingLock = new Object
  private var timingIndent = 0
  override def topLevelException(exception: Throwable): Unit = {
    exceptionLines(exception).foreach(emit)
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    val devon = devonMarshaller.fromValue(configuration)
    val pretty = devonMarshaller.toPretty(devon)
    emit("Effective configuration:")
    pretty.foreach(emit)
  }

  override def configurationError(lines: Seq[String]): Unit = {
    lines.foreach(emit)
  }

  override def startTiming(caption: String): Unit = {
    timingLock.synchronized {
      emit(indent(timingIndent) + s"start timer for '$caption'")
      timingIndent += 1
    }
  }

  override def endTiming(caption: String, duration: Duration): Unit = {
    timingLock.synchronized {
      timingIndent -= 1
      val formattedDuration = DurationFormat.MillisecondsFormat.format(duration.toMillis)
      emit(indent(timingIndent) + s"($formattedDuration) $caption")
    }
  }

  override def warnNoRelevantClassesInPath(path: Path): Unit = {
    emit(s"no relevant classes found in $path")
  }

  private def indent(indentLevel: Int): String = {
    "  " * indentLevel
  }

  private def exceptionLines(ex: Throwable): Seq[String] = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    ex.printStackTrace(printWriter)
    val s = stringWriter.toString
    val lines = s.split( """\r\n|\r|\n""").toSeq
    lines
  }
}
