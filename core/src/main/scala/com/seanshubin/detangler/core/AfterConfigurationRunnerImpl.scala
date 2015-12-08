package com.seanshubin.detangler.core

import java.nio.file.Path

class AfterConfigurationRunnerImpl(createReporter: (Detangled, Path) => Runnable, reportDir: Path) extends Runnable {
  override def run(): Unit = {
    createReporter(SampleData.detangled, reportDir).run()
  }
}
