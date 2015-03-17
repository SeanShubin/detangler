package com.seanshubin.detangler.core

trait RunnerFactory {
  def createRunner(configuration: Configuration): Runner
}
