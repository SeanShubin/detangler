package com.seanshubin.detangler.domain

case class StartsWithConfiguration(include: Seq[Seq[String]],
                                   exclude: Seq[Seq[String]],
                                   drop: Seq[Seq[String]]) {
  def replaceNullsWithDefaults(): StartsWithConfiguration = {
    val newInclude = Option(include).getOrElse(StartsWithConfiguration.Default.include)
    val newExclude = Option(exclude).getOrElse(StartsWithConfiguration.Default.exclude)
    val newDrop = Option(drop).getOrElse(StartsWithConfiguration.Default.drop)
    StartsWithConfiguration(newInclude, newExclude, newDrop)
  }
}

object StartsWithConfiguration {
  val Default = StartsWithConfiguration(
    include = Seq(),
    exclude = Seq(),
    drop = Seq()
  )
  val Sample = StartsWithConfiguration(
    include = Seq(Seq("com", "seanshubin"), Seq("seanshubin")),
    exclude = Seq(Seq("com", "seanshubin", "unchecked")),
    drop = Seq(Seq("com", "seanshubin"), Seq("seanshubin"))
  )
}
