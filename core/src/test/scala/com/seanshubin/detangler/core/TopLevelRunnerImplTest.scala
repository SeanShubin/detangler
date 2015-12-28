package com.seanshubin.detangler.core

import java.nio.file.Paths

import org.scalatest.FunSuite

class TopLevelRunnerImplTest extends FunSuite {
  test("valid configuration") {
    val args: Seq[String] = Seq("arg1")
    val validConfiguration = Configuration.Sample
    val validationSuccess = Right(validConfiguration)
    val configurationFactory = new ConfigurationFactoryStub(validationSuccess)
    val runnable = new RunnableStub
    val runnerFactory = (configuration: Configuration) => runnable
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
    val runnerFactory = (configuration: Configuration) => runnable
    val notifications = new NotificationsStub
    new TopLevelRunnerImpl(args, configurationFactory, runnerFactory, notifications).run()
    assert(configurationFactory.validateCalls === Seq(args))
    assert(notifications.invocations === Seq(("configurationError", errorLines)))
    assert(runnable.runCallCount === 0)
  }
}
