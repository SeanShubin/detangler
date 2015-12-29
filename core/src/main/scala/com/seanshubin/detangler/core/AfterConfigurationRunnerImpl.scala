package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.analysis.Detangler
import com.seanshubin.detangler.data.DependencyAccumulator
import com.seanshubin.detangler.model.{Detangled, Standalone}
import com.seanshubin.detangler.scanner.Scanner

class AfterConfigurationRunnerImpl(scanner: Scanner,
                                   detangler: Detangler,
                                   createReporter: (Detangled, Path) => Runnable,
                                   reportDir: Path,
                                   stringToStandalone: String => Option[Standalone]
                                  ) extends Runnable {
  override def run(): Unit = {
    val stringDependencies = scanner.scanDependencies()
    val standaloneDependencies = stringDependencies.flatMap(convertToStandaloneModule)
    val moduleAccumulator = DependencyAccumulator.fromIterable(standaloneDependencies)
    val detangled = detangler.analyze(moduleAccumulator.dependencies, moduleAccumulator.transpose().dependencies)
    val reporter = createReporter(detangled, reportDir)
    reporter.run()
    //    createReporter(DetangledFactory.contrivedSample(), reportDir.resolve("contrived")).run()
    //    createReporter(DetangledFactory.generatedSampleData(), reportDir.resolve("random")).run()
    //    createReporter(DetangledFactory.sampleWithoutCycles(), reportDir.resolve("simple")).run()
    //    createReporter(DetangledFactory.sampleWithCycles(), reportDir.resolve("cycles")).run()
  }

  private def convertToStandaloneModule(stringDependency: (String, Seq[String])): Option[(Standalone, Seq[Standalone])] = {
    val (stringKey, stringValues) = stringDependency
    val maybeStandaloneKey = stringToStandalone(stringKey)
    val standaloneValueOptions = stringValues.map(stringToStandalone)
    val standaloneValues = standaloneValueOptions.flatten
    maybeStandaloneKey.map((_, standaloneValues))
  }
}
