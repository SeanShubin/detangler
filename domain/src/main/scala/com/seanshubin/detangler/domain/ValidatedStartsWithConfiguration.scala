package com.seanshubin.detangler.domain

case class ValidatedStartsWithConfiguration(include: Seq[Seq[String]],
                                            exclude: Seq[Seq[String]],
                                            drop: Seq[Seq[String]])

object ValidatedStartsWithConfiguration {
  val Default = ValidatedStartsWithConfiguration(
    include = Seq(),
    exclude = Seq(),
    drop = Seq()
  )
  val Sample = ValidatedStartsWithConfiguration(
    include = Seq(Seq("com", "seanshubin"), Seq("seanshubin")),
    exclude = Seq(Seq("com", "seanshubin", "unchecked")),
    drop = Seq(Seq("com", "seanshubin"), Seq("seanshubin"))
  )
}
