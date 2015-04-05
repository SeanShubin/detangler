package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html.HtmlPage

trait ReportTransformer {
  def pageFor(detangled: Detangled, unitId: UnitId): HtmlPage

  def arrowId(from: UnitId, to: UnitId): String

  def arrowName(from: UnitId, to: UnitId): String
}
