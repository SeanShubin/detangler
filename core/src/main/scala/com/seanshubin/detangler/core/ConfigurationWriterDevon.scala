package com.seanshubin.detangler.core

import com.seanshubin.devon.core.devon.DevonMarshaller

class ConfigurationWriterDevon(configuration: Configuration,
                               devonMarshaller: DevonMarshaller) extends ConfigurationWriter {
  override def configurationLines(): Seq[String] = devonMarshaller.valueToPretty(configuration)

  override def configurationLinesAllowCycles(allowedInCycle: Seq[Seq[String]]): Seq[String] = {
    val configurationCopy = configuration.copy(allowedInCycle = allowedInCycle)
    devonMarshaller.valueToPretty(configurationCopy)
  }
}
