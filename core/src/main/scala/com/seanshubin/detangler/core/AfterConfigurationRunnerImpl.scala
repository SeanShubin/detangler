package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.model.Detangled

class AfterConfigurationRunnerImpl(createReporter: (Detangled, Path) => Runnable, reportDir: Path) extends Runnable {
  override def run(): Unit = {
    createReporter(DetangledFactory.generatedSampleData(), reportDir.resolve("random")).run()
    createReporter(DetangledFactory.sampleWithoutCycles(), reportDir.resolve("simple")).run()
    createReporter(DetangledFactory.sampleWithCycles(), reportDir.resolve("cycles")).run()
  }
}
