package com.seanshubin.detangler.timer

import java.time.{Clock, Duration}

class TimerImpl(clock: Clock, logTiming: Boolean, startEvent: String => Unit, endEvent: (String, Duration) => Unit) extends Timer {
  override def measureTime[T](name: String)(f: => T): T = {
    if (logTiming) {
      startEvent(name)
    }
    val before = clock.instant()
    val result = f
    val after = clock.instant()
    val duration = Duration.between(before, after)
    if (logTiming) {
      endEvent(name, duration)
    }
    result
  }
}
