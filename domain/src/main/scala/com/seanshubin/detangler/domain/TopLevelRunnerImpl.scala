package com.seanshubin.detangler.domain

class TopLevelRunnerImpl(args: Seq[String],
                         configurationFactory: ConfigurationFactory,
                         createRunner: (Configuration, Seq[Seq[String]]) => Runnable,
                         notifications: Notifications) extends Runnable {
  override def run(): Unit = {
    val errorOrConfiguration = configurationFactory.validate(args)
    errorOrConfiguration match {
      case Left(error) => notifications.configurationError(error)
      case Right((configuration, allowedCycles)) =>
        if (configuration.logEffectiveConfiguration.getOrElse(true)) {
          notifications.effectiveConfiguration(configuration)
        }
        createRunner(configuration, allowedCycles).run()
    }
  }
}
