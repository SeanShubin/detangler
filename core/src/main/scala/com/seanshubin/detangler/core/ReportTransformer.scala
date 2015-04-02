package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html.HtmlPage

trait ReportTransformer {
  def rootReport(detangled: Detangled): HtmlPage

  def htmlId(unitId: UnitId): String

  def htmlName(unitId: UnitId): String

  def htmlAnchor(unitId: UnitId): String

  def htmlFileName(unitId: UnitId): String

  def arrowId(from: UnitId, to: UnitId): String

  def arrowName(from: UnitId, to: UnitId): String
}
