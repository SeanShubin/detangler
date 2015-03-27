package com.seanshubin.detangler.core

case class UnitInfo(id: UnitId,
                    dependsOn: Set[UnitId],
                    dependedOnBy: Set[UnitId],
                    dependsOnExternal: Set[UnitId],
                    dependedOnByExternal: Set[UnitId],
                    composedOf: Set[UnitId],
                    depth: Int,
                    complexity: Int)
