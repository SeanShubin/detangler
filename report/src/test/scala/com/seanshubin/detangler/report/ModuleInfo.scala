package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Module, Single}

case class ModuleInfo(id: Module,
                      dependsOn: Set[Single] = Set(),
                      dependedOnBy: Set[Single] = Set(),
                      children: Set[Module] = Set(),
                      parts: Set[Single] = Set(),
                      depth: Int = 0,
                      complexity: Int = 0)
