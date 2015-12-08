package com.seanshubin.detangler.core

case class UnitInfo(id: UnitId,
                    dependsOn: Set[UnitId],
                    dependedOnBy: Set[UnitId],
                    composedOf: Set[UnitId],
                    depth: Int,
                    complexity: Int)
