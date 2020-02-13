package com.seanshubin.detangler.domain

case class StartsWithConfiguration(include: Seq[Seq[String]],
                                   exclude: Seq[Seq[String]],
                                   drop: Seq[Seq[String]])

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
