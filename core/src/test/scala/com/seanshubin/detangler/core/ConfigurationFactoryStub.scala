package com.seanshubin.detangler.core

import scala.collection.mutable.ArrayBuffer

class ConfigurationFactoryStub(validateResult: Either[Seq[String], Configuration]) extends ConfigurationFactory {
  val validateCalls = new ArrayBuffer[Seq[String]]

  override def validate(args: Seq[String]): Either[Seq[String], Configuration] = {
    validateCalls.append(args)
    validateResult
  }
}
