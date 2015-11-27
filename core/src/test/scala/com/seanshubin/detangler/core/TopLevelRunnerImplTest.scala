package com.seanshubin.detangler.core

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class TopLevelRunnerImplTest extends FunSuite with EasyMockSugar {
  test("valid configuration") {
    new Helper {
      override def expecting = () => {
        configurationFactory.validate(args).andReturn(validationSuccess)
        notifications.effectiveConfiguration(validConfiguration)
        runnerFactory(validConfiguration).andReturn(runner)
        runner.run()
      }

      override def whenExecuting = () => {
        launcher.run()
      }
    }
  }

  test("invalid configuration") {
    new Helper {
      override def expecting = () => {
        configurationFactory.validate(args).andReturn(validationFailure)
        notifications.configurationError(errorLines)
      }

      override def whenExecuting = () => {
        launcher.run()
      }
    }
  }

  trait Helper {
    val args: Seq[String] = Seq("arg1")
    val validConfiguration = Configuration(Paths.get("generated", "report"))
    val validationSuccess = Right(validConfiguration)
    val errorLines = Seq("error")
    val validationFailure = Left(errorLines)
    val configurationFactory = mock[ConfigurationFactory]
    val runnerFactory = mock[Configuration => Runnable]
    val notifications = mock[Notifications]
    val runner = mock[Runnable]
    val launcher = new TopLevelRunnerImpl(args, configurationFactory, runnerFactory, notifications)

    def expecting: () => Unit

    def whenExecuting: () => Unit

    expecting()
    EasyMockSugar.whenExecuting(configurationFactory, runnerFactory, notifications, runner) {
      whenExecuting()
    }
  }

}
