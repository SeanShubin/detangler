package com.seanshubin.detangler.core

trait ConfigurationFactory {
  def validate(args: Seq[String]): Either[Seq[String], Configuration]
}
