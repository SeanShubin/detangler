package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.analysis.Detangler
import com.seanshubin.detangler.data.DependencyAccumulator
import com.seanshubin.detangler.model.{Detangled, Standalone}
import com.seanshubin.detangler.scanner.Scanner
import com.seanshubin.detangler.timer.Timer

class AfterConfigurationRunnerImpl(scanner: Scanner,
                                   detangler: Detangler,
                                   createReporter: (Detangled, Path) => Runnable,
                                   reportDir: Path,
                                   stringToStandalone: String => Option[Standalone],
                                   notifications: Notifications,
                                   timer: Timer) extends Runnable {
  override def run(): Unit = {
    val (timeTaken, _) = timer.measureTime {
      val (scanTime, stringDependencies) = timer.measureTime {
        scanner.scanDependencies()
      }
      notifications.timeTaken("scanner", scanTime)

      val (accumulatorTime, moduleAccumulator) = timer.measureTime {
        val standaloneDependencies = stringDependencies.flatMap(convertToStandaloneModule)
        DependencyAccumulator.fromIterable(standaloneDependencies)
      }
      notifications.timeTaken("accumulator", accumulatorTime)

      val (detangledTime, detangled) = timer.measureTime {
        detangler.analyze(moduleAccumulator.dependencies, moduleAccumulator.transpose().dependencies)
      }
      notifications.timeTaken("detangler", detangledTime)

      val (reporterTime, _) = timer.measureTime {
        val reporter = createReporter(detangled, reportDir)
        reporter.run()
      }
      notifications.timeTaken("reporter", reporterTime)
    }
    notifications.timeTaken("total", timeTaken)

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
