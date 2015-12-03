package com.seanshubin.detangler.core

import org.jsoup.nodes.{Document, Element}

case class Templates(unitDependencyRow: Element,
                     unitDependency: Element,
                     unitSummary: Element,
                     unitDiv: Element,
                     reasons: Element,
                     reason: Element)

object Templates {
  def fromDocument(document: Document, shouldRemoveClass: Boolean): Templates = {
    val jsoupUtil = new JsoupUtil(shouldRemoveClass)
    val unitDependencyRow = jsoupUtil.extractFragment(document, "unit-dependency-row")
    val unitDependency = jsoupUtil.extractFragment(document, "unit-dependency")
    val unitSummary = jsoupUtil.extractFragment(document, "unit-summary")
    val unitDiv = jsoupUtil.extractFragment(document, "unit-root")
    val reason = jsoupUtil.extractFragment(document, "reason")
    val reasons = jsoupUtil.extractFragment(document, "reasons")
    Templates(unitDependencyRow, unitDependency, unitSummary, unitDiv, reasons, reason)
  }
}
