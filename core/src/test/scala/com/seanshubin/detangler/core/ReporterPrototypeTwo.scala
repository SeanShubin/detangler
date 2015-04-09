package com.seanshubin.detangler.core

object ReporterPrototypeTwo extends App with PrototypeWiring {
  override def dirName: String = "2"

  reporter.generateReportsTwo(SampleData.detangled)
}
