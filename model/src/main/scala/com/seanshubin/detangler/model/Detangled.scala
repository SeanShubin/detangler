package com.seanshubin.detangler.model

import scala.collection.parallel.immutable.ParSet

trait Detangled {
  def root(): Single

  def children(module: Single): ParSet[Module]
}
