package com.seanshubin.detangler.domain

import scala.collection.mutable.ArrayBuffer

class ConfigurationFactoryStub(validateResult: Either[Seq[String], (Configuration, Seq[Seq[String]])]) extends ConfigurationFactory {
  val validateCalls = new ArrayBuffer[Seq[String]]

  override def validate(args: Seq[String]): Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    validateCalls.append(args)
    validateResult
  }
}
