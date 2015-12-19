package com.seanshubin.detangler.model

object ModuleOrdering {
  def lessThan(leftModule: Module, rightModule: Module): Boolean = {
    (leftModule, rightModule) match {
      case (left: Standalone, right: Standalone) => left < right
      case (left: Standalone, right: Cycle) => false
      case (left: Cycle, right: Standalone) => true
      case (left: Cycle, right: Cycle) => left.parts.head < right.parts.head
    }
  }
}
