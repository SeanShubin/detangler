package com.seanshubin.detangler.model

trait Detangled {
  def root(): Single

  def children(module: Single): Set[Module]
}
