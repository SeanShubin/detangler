package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.model.Detangled

class AfterConfigurationRunnerImpl(createReporter: (Detangled, Path) => Runnable, reportDir: Path) extends Runnable {
  override def run(): Unit = {
    createReporter(DetangledFactory.sampleWithoutCycles(), reportDir.resolve("cycles-false")).run()
    createReporter(DetangledFactory.sampleWithCycles(), reportDir.resolve("cycles-true")).run()
  }
}
