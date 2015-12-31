package com.seanshubin.detangler.core

import java.time.Duration

trait Timer {
  def measureTime(block: => Unit): Duration
}
