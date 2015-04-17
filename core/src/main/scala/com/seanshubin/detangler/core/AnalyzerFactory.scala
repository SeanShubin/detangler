package com.seanshubin.detangler.core

trait AnalyzerFactory {
  def createRunner(configuration: Configuration): Analyzer
}
