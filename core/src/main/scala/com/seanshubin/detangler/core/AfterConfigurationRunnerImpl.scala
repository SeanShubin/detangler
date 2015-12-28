package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.analysis.Detangler
import com.seanshubin.detangler.data.DependencyAccumulator
import com.seanshubin.detangler.model.Detangled
import com.seanshubin.detangler.scanner.Scanner

class AfterConfigurationRunnerImpl(scanner: Scanner,
                                   detangler: Detangler,
                                   createReporter: (Detangled, Path) => Runnable,
                                   reportDir: Path) extends Runnable {
  override def run(): Unit = {
    val dependencies = scanner.scanDependencies()
    val accumulator = DependencyAccumulator.fromIterable(dependencies)
    val detangled = detangler.analyze(accumulator.dependencies, accumulator.transpose().dependencies)
    val reporter = createReporter(detangled, reportDir)
    reporter.run()
    //    createReporter(DetangledFactory.contrivedSample(), reportDir.resolve("contrived")).run()
    //    createReporter(DetangledFactory.generatedSampleData(), reportDir.resolve("random")).run()
    //    createReporter(DetangledFactory.sampleWithoutCycles(), reportDir.resolve("simple")).run()
    //    createReporter(DetangledFactory.sampleWithCycles(), reportDir.resolve("cycles")).run()
  }
}
