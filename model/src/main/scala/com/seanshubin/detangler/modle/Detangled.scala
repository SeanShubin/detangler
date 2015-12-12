package com.seanshubin.detangler.modle

import scala.collection.parallel.immutable.ParSet

trait Detangled {
  def root(): Module

  def children(module: Module): ParSet[Module]
}
