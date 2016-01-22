package com.seanshubin.detangler.timer

trait Timer {
  def measureTime[T](name: String)(f: => T): T
}

object Timer {
  val DoNothing:Timer = new Timer {
    override def measureTime[T](name: String)(f: => T): T = f
  }
}