package com.seanshubin.detangler.core

import java.time.{Clock, Duration}

class TimerImpl(clock: Clock) extends Timer {
  override def measureTime(block: => Unit): Duration = {
    val before = clock.instant()
    block
    val after = clock.instant()
    val duration = Duration.between(before, after)
    duration
  }
}
