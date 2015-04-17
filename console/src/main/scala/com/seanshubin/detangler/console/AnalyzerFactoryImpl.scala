package com.seanshubin.detangler.console

import com.seanshubin.detangler.core.{Configuration, Analyzer, AnalyzerFactory}

class AnalyzerFactoryImpl extends AnalyzerFactory {
  override def createRunner(theConfiguration: Configuration): Analyzer = {
    new AnalyzerWiring {
      override def configuration: Configuration = theConfiguration
    }.runner
  }
}
