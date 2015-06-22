package com.seanshubin.detangler.core

import org.jsoup.nodes.{Document, Element}

case class Templates(unitDependencyRow: Element,
                unitDependency: Element,
                unitSummary: Element,
                unitDiv: Element,
                reasons: Element,
                reason: Element)

object Templates {
  def fromDocument(document:Document, shouldRemoveClass: Boolean):Templates = {
    val unitDependencyRow = JsoupUtil.extractFragment(document, "unit-dependency-row", shouldRemoveClass)
    val unitDependency = JsoupUtil.extractFragment(document, "unit-dependency", shouldRemoveClass)
    val unitSummary = JsoupUtil.extractFragment(document, "unit-summary", shouldRemoveClass)
    val unitDiv = JsoupUtil.extractFragment(document, "unit-root", shouldRemoveClass)
    val reason = JsoupUtil.extractFragment(document, "reason", shouldRemoveClass)
    val reasons = JsoupUtil.extractFragment(document, "reasons", shouldRemoveClass)
    Templates(unitDependencyRow, unitDependency, unitSummary, unitDiv, reasons, reason)
  }
}
