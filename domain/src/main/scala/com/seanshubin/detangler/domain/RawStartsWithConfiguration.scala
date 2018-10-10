package com.seanshubin.detangler.domain

case class RawStartsWithConfiguration(include: Option[Seq[Seq[String]]],
                                   exclude: Option[Seq[Seq[String]]],
                                   drop: Option[Seq[Seq[String]]]) {
  def replaceEmptyWithDefaults(): ValidatedStartsWithConfiguration = {
    val newInclude = include.getOrElse(ValidatedStartsWithConfiguration.Default.include)
    val newExclude = exclude.getOrElse(ValidatedStartsWithConfiguration.Default.exclude)
    val newDrop = drop.getOrElse(ValidatedStartsWithConfiguration.Default.drop)
    ValidatedStartsWithConfiguration(newInclude, newExclude, newDrop)
  }
}
