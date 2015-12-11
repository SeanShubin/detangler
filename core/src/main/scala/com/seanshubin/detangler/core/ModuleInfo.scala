package com.seanshubin.detangler.core

case class ModuleInfo(id: Module,
                      dependsOn: Set[Module],
                      dependedOnBy: Set[Module],
                      children: Set[Module],
                      cycleParts: Set[Module],
                      depth: Int,
                      complexity: Int)
