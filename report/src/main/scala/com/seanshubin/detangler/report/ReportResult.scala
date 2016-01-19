package com.seanshubin.detangler.report

trait ReportResult

object ReportResult {

  case object Success extends ReportResult

  case class Failure(message: String) extends ReportResult

}
