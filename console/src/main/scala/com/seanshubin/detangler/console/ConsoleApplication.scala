package com.seanshubin.detangler.console

object ConsoleApplication extends App with ConfigurationWiring {
  override def commandLineArguments: Seq[String] = args

  launcher.launch()
}
