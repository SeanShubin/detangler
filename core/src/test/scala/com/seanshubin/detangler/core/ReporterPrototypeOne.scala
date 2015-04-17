package com.seanshubin.detangler.core

object ReporterPrototypeOne extends App with PrototypeWiring {
  override def dirName: String = "1"

  reporter.generateReportsOne()

}
