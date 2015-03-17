package com.seanshubin.detangler.core

class RunnerImpl(target: String, emitLine: String => Unit) extends Runner {
  override def run(): Unit = emitLine(s"Hello, $target!")
}
