package com.seanshubin.detangler.graphviz

trait GraphGenerator {
  def generate(dependsOn: Map[String, Set[String]], cycles: Map[String, Set[String]]): Iterable[String]
}
