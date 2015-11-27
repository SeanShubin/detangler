package com.seanshubin.detangler.console

object ConsoleApplication extends App {
  new TopLevelWiring {
    override def commandLineArguments: Seq[String] = args
  }.launcher.run()
}
