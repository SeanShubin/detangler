package com.seanshubin.detangler.report

import java.nio.file.Path

import com.seanshubin.detangler.model.Standalone

trait ReportResult

object ReportResult {

  case class Success(reportIndex: Path, newCycleParts: Seq[Standalone]) extends ReportResult

  case class Failure(reportIndex: Path, message: String, newCycleParts: Seq[Standalone]) extends ReportResult
}
