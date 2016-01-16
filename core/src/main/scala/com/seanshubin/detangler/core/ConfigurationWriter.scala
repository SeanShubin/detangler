package com.seanshubin.detangler.core

trait ConfigurationWriter {
  def configurationLines(): Seq[String]

  def configurationLinesAllowCycles(allowedInCycle: Seq[Seq[String]]): Seq[String]
}
