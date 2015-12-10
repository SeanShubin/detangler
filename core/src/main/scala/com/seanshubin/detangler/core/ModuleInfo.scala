package com.seanshubin.detangler.core

case class ModuleInfo(id: Module,
                      dependsOn: Set[Module],
                      dependedOnBy: Set[Module],
                      composedOf: Set[Module],
                      depth: Int,
                      complexity: Int)
