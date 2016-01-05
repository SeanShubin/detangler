package com.seanshubin.detangler.timer

import java.time.{Clock, Duration}

class TimerImpl(clock: Clock) extends Timer {
  override def measureTime[T](block: => T): (Duration, T) = {
    val before = clock.instant()
    val result = block
    val after = clock.instant()
    val duration = Duration.between(before, after)
    (duration, result)
  }
}
