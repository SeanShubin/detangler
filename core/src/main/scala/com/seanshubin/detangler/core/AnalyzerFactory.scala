package com.seanshubin.detangler.core

trait AnalyzerFactory {
  def createAnalyzer(configuration: Configuration): Analyzer
}
