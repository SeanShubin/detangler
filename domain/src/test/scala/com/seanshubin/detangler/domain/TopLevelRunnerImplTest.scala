package com.seanshubin.detangler.domain

import org.scalatest.FunSuite

class TopLevelRunnerImplTest extends FunSuite {
  test("valid configuration") {
    val args: Seq[String] = Seq("arg1")
    val validConfiguration = Configuration.Sample
    val emptyAllowedCycles: Seq[Seq[String]] = Seq()
    val validationSuccess = Right(validConfiguration, emptyAllowedCycles)
    val configurationFactory = new ConfigurationFactoryStub(validationSuccess)
    val runnable = new RunnableStub
    val runnerFactory = (configuration: Configuration, allowedCycles: Seq[Seq[String]]) => runnable
    val notifications = new NotificationsStub
    new TopLevelRunnerImpl(args, configurationFactory, runnerFactory, notifications).run()
    assert(configurationFactory.validateCalls === Seq(args))
    assert(notifications.invocations === Seq(("effectiveConfiguration", validConfiguration)))
    assert(runnable.runCallCount === 1)
  }

  test("invalid configuration") {
    val args: Seq[String] = Seq("arg1")
    val errorLines = Seq("error")
    val validationFailure = Left(errorLines)
    val configurationFactory = new ConfigurationFactoryStub(validationFailure)
    val runnable = new RunnableStub
    val runnerFactory = (configuration: Configuration, allowedCycles: Seq[Seq[String]]) => runnable
    val notifications = new NotificationsStub
    new TopLevelRunnerImpl(args, configurationFactory, runnerFactory, notifications).run()
    assert(configurationFactory.validateCalls === Seq(args))
    assert(notifications.invocations === Seq(("configurationError", errorLines)))
    assert(runnable.runCallCount === 0)
  }
}
