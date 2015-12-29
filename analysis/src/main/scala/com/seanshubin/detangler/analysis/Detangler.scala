package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Detangled, Standalone}

trait Detangler {
  def analyze(dependsOn: Map[Standalone, Set[Standalone]], dependedOnBy: Map[Standalone, Set[Standalone]]): Detangled
}
