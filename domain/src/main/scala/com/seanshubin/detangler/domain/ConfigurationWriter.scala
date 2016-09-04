package com.seanshubin.detangler.domain

trait ConfigurationWriter {
  def configurationLines(): Seq[String]

  def configurationLinesAllowCycles(allowedInCycle: Seq[Seq[String]]): Seq[String]
}
