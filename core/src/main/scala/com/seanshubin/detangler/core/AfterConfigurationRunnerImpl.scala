package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.analysis.Detangler
import com.seanshubin.detangler.data.DependencyAccumulator
import com.seanshubin.detangler.model.{Detangled, Standalone}
import com.seanshubin.detangler.report.ReportResult
import com.seanshubin.detangler.scanner.Scanner
import com.seanshubin.detangler.timer.Timer

class AfterConfigurationRunnerImpl(scanner: Scanner,
                                   detangler: Detangler,
                                   createReporter: (Detangled, Path, Seq[Standalone], Notifications) => () => ReportResult,
                                   reportDir: Path,
                                   allowedCyclesAsStrings: Seq[Seq[String]],
                                   stringToStandalone: String => Option[Standalone],
                                   timer: Timer,
                                   notifications: Notifications) extends Runnable {
  override def run(): Unit = {
    val reportResult: ReportResult = timer.measureTime("total") {
      val stringDependencies = timer.measureTime("scanner")(scanner.scanDependencies())
      val moduleAccumulator = timer.measureTime("accumulator") {
        val standaloneDependencies = stringDependencies.flatMap(convertToStandaloneModule)
        DependencyAccumulator.fromIterable(standaloneDependencies)
      }
      val detangled = timer.measureTime("detangler") {
        detangler.analyze(moduleAccumulator.dependencies, moduleAccumulator.transpose().dependencies)
      }
      val allowedCycles: Seq[Standalone] = allowedCyclesAsStrings.map(Standalone.apply)
      timer.measureTime("reporter") {
        val reporter = createReporter(detangled, reportDir, allowedCycles, notifications)
        reporter.apply()
      }
    }
    reportResult match {
      case ReportResult.Failure(message) =>
        throw new RuntimeException(message)
      case ReportResult.Success =>
      //do nothing
    }
  }

  private def convertToStandaloneModule(stringDependency: (String, Seq[String])): Option[(Standalone, Seq[Standalone])] = {
    val (stringKey, stringValues) = stringDependency
    val maybeStandaloneKey = stringToStandalone(stringKey)
    val standaloneValueOptions = stringValues.map(stringToStandalone)
    val standaloneValues = standaloneValueOptions.flatten
    maybeStandaloneKey.map((_, standaloneValues))
  }
}
