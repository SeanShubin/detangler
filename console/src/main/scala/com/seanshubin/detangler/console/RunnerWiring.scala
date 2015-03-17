package com.seanshubin.detangler.console

import com.seanshubin.detangler.core._

trait RunnerWiring {
  def configuration: Configuration

  lazy val emitLine: String => Unit = println
  lazy val runner: Runner = new RunnerImpl(configuration.greetingTarget, emitLine)
}
