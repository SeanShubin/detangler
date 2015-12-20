package com.seanshubin.detangler.model

case class ModuleInfo(id: Module,
                      dependsOn: Set[Standalone] = Set(),
                      dependedOnBy: Set[Standalone] = Set(),
                      children: Set[Module] = Set(),
                      parts: Set[Standalone] = Set(),
                      depth: Int = 0,
                      complexity: Int = 0)
