package com.seanshubin.detangler.domain

import java.io.{PrintWriter, StringWriter}
import java.nio.file.Path
import java.time.Duration

import com.seanshubin.detangler.model.Standalone
import com.seanshubin.devon.domain.DevonMarshaller

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
    emit(s"WARNING: no relevant classes found in $path, this probably warrants configuring your includes, excludes, or ignored files")
  }

  override def reportGenerated(indexPath: Path, cycleParts: Seq[Standalone]): Unit = {
    if (cycleParts.isEmpty) {
      emit(s"SUCCESS: see report at $indexPath")
    } else {
      val cycleName = if (cycleParts.size == 1) "cycle" else "cycles"
      emit(s"FAILURE: ${cycleParts.size} new $cycleName")
      cycleParts.map(_.toString).foreach(emit)
    }
  }

  private def exceptionLines(ex: Throwable): Seq[String] = {
    val stringWriter = new StringWriter()
    val printWriter = new PrintWriter(stringWriter)
    ex.printStackTrace(printWriter)
    val s = stringWriter.toString
    val lines = s.split( """\r\n|\r|\n""").toSeq
    lines
  }

  private def indent(indentLevel: Int): String = {
    "  " * indentLevel
  }
}
