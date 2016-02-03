package com.seanshubin.detangler.report

import java.nio.file.Path

trait ReportResult

object ReportResult {

  case class Success(reportDir: Path) extends ReportResult

  case class Failure(message: String) extends ReportResult

}
