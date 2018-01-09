package com.seanshubin.detangler.domain

trait ConfigurationFactory {
  def validate(args: Seq[String]): Either[Seq[String], (Configuration, Seq[Seq[String]])]
}
