package com.seanshubin.detangler.model

object ModuleOrdering {
  def lessThan(leftModule: Module, rightModule: Module): Boolean = {
    (leftModule, rightModule) match {
      case (left: Single, right: Single) => left < right
      case (left: Single, right: Cycle) => false
      case (left: Cycle, right: Single) => true
      case (left: Cycle, right: Cycle) => left.parts.head < right.parts.head
    }
  }
}
