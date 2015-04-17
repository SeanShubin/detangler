package com.seanshubin.detangler.console

import java.nio.file.Path

import com.seanshubin.detangler.core.{Configuration, Analyzer, AnalyzerFactory}

class AnalyzerFactoryImpl extends AnalyzerFactory {
  override def createAnalyzer(theConfiguration: Configuration): Analyzer = {
    new AnalyzerWiring {
      override def reportDir: Path = theConfiguration.reportDir
    }.analyzer
  }
}
