package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Detangled

trait Detangler {
  def analyze(dependsOn: Map[Seq[String], Set[Seq[String]]], dependedOnBy: Map[Seq[String], Set[Seq[String]]]): Detangled
}
