package com.seanshubin.detangler.console

object ConsoleApplication extends App with TopLevelWiring {
  override def commandLineArguments: Seq[String] = args

  launcher.apply()
}
