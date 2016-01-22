package com.seanshubin.detangler.core

import java.nio.file.Path

import com.seanshubin.detangler.analysis.Detangler
import com.seanshubin.detangler.data.DependencyAccumulator
import com.seanshubin.detangler.model.{SourceAndDependencies, SourceAndDependencies$, Detangled, Standalone}
import com.seanshubin.detangler.report.ReportResult
import com.seanshubin.detangler.scanner.{ScannedDependencies, Scanner}
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
        val standaloneDependencies:Iterable[SourceAndDependencies] = stringDependencies.flatMap(convertToStandaloneModule)

        DependencyAccumulator.fromIterable(standaloneDependencies.map(_.dependencies))
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

  private def convertToStandaloneModule(stringDependency: ScannedDependencies): Option[SourceAndDependencies] = {
    val ScannedDependencies(sourceName, stringKey, stringValues) = stringDependency
    val maybeStandaloneKey = stringToStandalone(stringKey)
    val standaloneValueOptions = stringValues.map(stringToStandalone)
    val standaloneValues = standaloneValueOptions.flatten
    def createFromKey(standalone:Standalone):SourceAndDependencies = {
      val dependencies = Map(standalone -> standaloneValues.toSet)
      SourceAndDependencies(sourceName, dependencies)
    }
    maybeStandaloneKey.map(createFromKey)
  }
}
