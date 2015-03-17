package com.seanshubin.detangler.console

object ConsoleApplication extends App with LauncherWiring {
  override def commandLineArguments: Seq[String] = args

  launcher.launch()
}
