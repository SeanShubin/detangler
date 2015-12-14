package com.seanshubin.detangler.core

case class ModuleInfo(id: Module,
                      dependsOn: Set[Module] = Set(),
                      dependedOnBy: Set[Module] = Set(),
                      children: Set[Module] = Set(),
                      cycleParts: Set[Module] = Set(),
                      depth: Int = 0,
                      complexity: Int = 0)
