package com.seanshubin.detangler.report

import java.nio.file.Path

trait ReportResult

object ReportResult {

  case class Success(reportIndex: Path) extends ReportResult

  case class Failure(reportIndex: Path, message: String) extends ReportResult
}
