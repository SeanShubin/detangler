package com.seanshubin.detangler.console

import com.seanshubin.detangler.core.{Configuration, Runner, RunnerFactory}

class RunnerFactoryImpl extends RunnerFactory {
  override def createRunner(theConfiguration: Configuration): Runner = {
    new RunnerWiring {
      override def configuration: Configuration = theConfiguration
    }.runner
  }
}
