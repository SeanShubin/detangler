package com.seanshubin.detangler.timer

import java.time.Duration

trait Timer {
  def measureTime[T](block: => T): (Duration, T)
}
