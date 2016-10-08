package com.seanshubin.detangler.domain

import com.seanshubin.detangler.compare.Compare
import com.seanshubin.devon.parserules.DevonMarshaller

class ConfigurationWriterDevon(configuration: Configuration,
                               devonMarshaller: DevonMarshaller) extends ConfigurationWriter {
  override def configurationLines(): Seq[String] = devonMarshaller.valueToPretty(configuration)

  override def configurationLinesAllowCycles(allowedInCycle: Seq[Seq[String]]): Seq[String] = {
    val compareSeqFunction = Compare.composeCompareSeqFunction(Ordering.String.compare)
    val seqLessThanFunction = Compare.lessThan(compareSeqFunction)
    val sortedAllowedInCycle = listify(allowedInCycle).sortWith(seqLessThanFunction)
    val configurationCopy = configuration.copy(allowedInCycle = sortedAllowedInCycle)
    devonMarshaller.valueToPretty(configurationCopy)
  }

  private def listify(seq: Seq[Seq[String]]): List[List[String]] = seq.map(_.toList).toList
}
