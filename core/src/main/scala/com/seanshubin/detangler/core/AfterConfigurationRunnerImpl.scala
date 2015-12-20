package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.model.{Detangled, SampleData, SampleDataWithCycles}

class AfterConfigurationRunnerImpl(createReporter: (Detangled, Path) => Runnable, reportDir: Path) extends Runnable {
  override def run(): Unit = {
    createReporter(SampleData.detangled, reportDir.resolve("cycles-false")).run()
    createReporter(SampleDataWithCycles.detangled, reportDir.resolve("cycles-true")).run()
  }
}
