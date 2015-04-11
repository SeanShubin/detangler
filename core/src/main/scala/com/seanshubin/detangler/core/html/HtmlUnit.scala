package com.seanshubin.detangler.core.html

case class HtmlUnit(id: String,
                    name: String,
                    depth: String,
                    complexity: String,
                    partsAnchor: HtmlAnchor,
                    dependsOn: Seq[HtmlUnitLink],
                    dependedOnBy: Seq[HtmlUnitLink],
                    dependsOnExternal: Seq[HtmlUnitLink],
                    dependedOnByExternal: Seq[HtmlUnitLink]) {
  def dependsOnCaption: String = s"depends on (${dependsOn.size})"

  def dependedOnByCaption: String = s"depended on by (${dependedOnBy.size})"

  def dependsOnExternalCaption: String = s"depends on external (${dependsOnExternal.size})"

  def dependedOnByExternalCaption: String = s"depended on by external (${dependedOnByExternal.size})"
}
