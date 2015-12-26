package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.model.Detangled

class AfterConfigurationRunnerImpl(createReporter: (Detangled, Path) => Runnable, reportDir: Path) extends Runnable {
  override def run(): Unit = {
    val detangledFactory = new DetangledFactory
    createReporter(detangledFactory.generatedSampleData(), reportDir.resolve("random")).run()
    createReporter(detangledFactory.sampleWithoutCycles(), reportDir.resolve("simple")).run()
    createReporter(detangledFactory.sampleWithCycles(), reportDir.resolve("cycles")).run()
  }
}
