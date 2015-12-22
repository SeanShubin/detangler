package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.{Detangled, Standalone}

trait Detangler {
  def analyze(data: Seq[(Standalone, Standalone)]): Detangled
}
