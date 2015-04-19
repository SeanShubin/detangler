package com.seanshubin.detangler.core

class LauncherImpl(args: Seq[String],
                   configurationFactory: ConfigurationFactory,
                   analyzerFactory: AnalyzerFactory,
                   notifications: Notifications) extends Launcher {
  override def launch(): Unit = {
    val errorOrConfiguration = configurationFactory.validate(args)
    errorOrConfiguration match {
      case Left(error) => notifications.configurationError(error)
      case Right(configuration) =>
        notifications.effectiveConfiguration(configuration)
        analyzerFactory.createAnalyzer(configuration).analyze()
    }
  }
}
