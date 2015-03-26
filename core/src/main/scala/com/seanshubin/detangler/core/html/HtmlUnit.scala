package com.seanshubin.detangler.core.html

case class HtmlUnit(id: String,
                    name: String,
                    depth: String,
                    complexity: String,
                    partsAnchor: HtmlAnchor,
                    dependsOn: Seq[HtmlUnitLink],
                    dependedOnBy: Seq[HtmlUnitLink],
                    dependsOnExternal: Seq[HtmlUnitLink],
                    dependedOnByExternal: Seq[HtmlUnitLink])