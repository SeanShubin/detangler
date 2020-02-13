package com.seanshubin.detangler.domain

case class RawStartsWithConfiguration(include: Option[Seq[Seq[String]]],
                                   exclude: Option[Seq[Seq[String]]],
                                   drop: Option[Seq[Seq[String]]]) {
  def replaceEmptyWithDefaults(): StartsWithConfiguration = {
    val newInclude = include.getOrElse(StartsWithConfiguration.Default.include)
    val newExclude = exclude.getOrElse(StartsWithConfiguration.Default.exclude)
    val newDrop = drop.getOrElse(StartsWithConfiguration.Default.drop)
    StartsWithConfiguration(newInclude, newExclude, newDrop)
  }
}
