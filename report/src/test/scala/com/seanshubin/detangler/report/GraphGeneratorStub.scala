package com.seanshubin.detangler.report

import com.seanshubin.detangler.graphviz.GraphGenerator

class GraphGeneratorStub(graphLines: Iterable[String]) extends GraphGenerator {
  override def generate(dependsOn: Map[String, Set[String]], cycles: Map[String, Set[String]]): Iterable[String] = {
    graphLines
  }
}
