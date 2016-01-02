package com.seanshubin.detangler.graphviz

import com.seanshubin.detangler.collection.SeqDifference
import org.scalatest.FunSuite

class GraphGeneratorTest extends FunSuite {
  test("no cycles") {
    val graphGenerator: GraphGenerator = new GraphGeneratorImpl
    val dependsOn: Map[String, Set[String]] = Map(
      "a" -> Set("b", "c"),
      "b" -> Set("c", "d"),
      "c" -> Set("d"),
      "d" -> Set())
    val cycles: Map[String, Set[String]] = Map()
    val actual = graphGenerator.generate(dependsOn, cycles)
    val expected =
      """digraph detangled {
        |  "a";
        |  "b";
        |  "c";
        |  "d";
        |  "a" -> "b";
        |  "a" -> "c";
        |  "b" -> "c";
        |  "b" -> "d";
        |  "c" -> "d";
        |}""".stripMargin.split("\r\n|\r|\n")
    val result = SeqDifference.diff(actual, expected)
    assert(result.isSame, result.messageLines.mkString("\n"))
  }

  test("cycles") {
    val graphGenerator: GraphGenerator = new GraphGeneratorImpl
    val dependsOn: Map[String, Set[String]] = Map(
      "a" -> Set("b"),
      "b" -> Set("c"),
      "c" -> Set("b", "d"),
      "d" -> Set())
    val cycles: Map[String, Set[String]] = Map(
      "b" -> Set("b", "c"),
      "c" -> Set("b", "c"))
    val actual = graphGenerator.generate(dependsOn, cycles)
    val expected =
      """digraph detangled {
        |  "a";
        |  "b";
        |  "c";
        |  "d";
        |  "a" -> "b";
        |  subgraph "cluster_b" {
        |    penwidth=2;
        |    pencolor=Red;
        |    "b" -> "c";
        |    "c" -> "b";
        |  }
        |  "c" -> "d";
        |}""".stripMargin.split("\r\n|\r|\n")
    val result = SeqDifference.diff(actual, expected)
    assert(result.isSame, result.messageLines.mkString("\n"))
  }

  test("moderately complex sample") {
    val graphGenerator: GraphGenerator = new GraphGeneratorImpl
    val dependsOn: Map[String, Set[String]] = Map(
      "a" -> Set("b"),
      "b" -> Set("c", "d"),
      "c" -> Set(),
      "d" -> Set("e"),
      "e" -> Set("f", "g"),
      "f" -> Set("d"),
      "g" -> Set("h"),
      "h" -> Set("g", "i", "j"),
      "i" -> Set("j"),
      "j" -> Set(),
      "k" -> Set()
    )
    val cycles: Map[String, Set[String]] = Map(
      "d" -> Set("d", "e", "f"),
      "e" -> Set("d", "e", "f"),
      "f" -> Set("d", "e", "f"),
      "g" -> Set("g", "h"),
      "h" -> Set("g", "h")
    )
    val actual = graphGenerator.generate(dependsOn, cycles)
    val expected =
      """digraph detangled {
        |  "a";
        |  "b";
        |  "c";
        |  "d";
        |  "e";
        |  "f";
        |  "g";
        |  "h";
        |  "i";
        |  "j";
        |  "k";
        |  "a" -> "b";
        |  "b" -> "c";
        |  "b" -> "d";
        |  subgraph "cluster_d" {
        |    penwidth=2;
        |    pencolor=Red;
        |    "d" -> "e";
        |    "e" -> "f";
        |    "f" -> "d";
        |  }
        |  "e" -> "g";
        |  subgraph "cluster_g" {
        |    penwidth=2;
        |    pencolor=Red;
        |    "g" -> "h";
        |    "h" -> "g";
        |  }
        |  "h" -> "i";
        |  "h" -> "j";
        |  "i" -> "j";
        |}""".stripMargin.split("\r\n|\r|\n")
    val result = SeqDifference.diff(actual, expected)
    assert(result.isSame, result.messageLines.mkString("\n"))
  }
}
