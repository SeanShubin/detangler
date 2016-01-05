package com.seanshubin.detangler.timer

trait Timer {
  def measureTime[T](name: String)(f: => T): T
}
