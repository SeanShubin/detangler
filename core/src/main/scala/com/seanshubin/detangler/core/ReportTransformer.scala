package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html.HtmlPage

trait ReportTransformer {
  def pageFor(detangled: Detangled, unitId: UnitId): HtmlPage

  def htmlId(unitId: UnitId): String

  def htmlName(unitId: UnitId): String

  def htmlLinkRelative(unitId: UnitId): String

  def htmlLinkAbsolute(unitId: UnitId): String

  def htmlFileName(unitId: UnitId): String

  def arrowId(from: UnitId, to: UnitId): String

  def arrowName(from: UnitId, to: UnitId): String
}
