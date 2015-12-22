package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Detangled

trait Detangler {
  def analyze(data: Seq[(String, String)]): Detangled
}
