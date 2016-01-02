package com.seanshubin.detangler.graphviz

class GraphGeneratorImpl extends GraphGenerator {
  def generate(dependsOn: Map[String, Set[String]], cycles: Map[String, Set[String]]): Iterable[String] = {
    val sortedKeys = dependsOn.keys.toSeq.sorted
    val singleKeys = for {
      key <- sortedKeys
    } yield {
      SingleGenerator(key)
    }
    def addDependency(soFar: Accumulator, key: String): Accumulator = {
      val generators = for {
        value <- dependsOn(key).toSeq.sorted
        if !cycles.getOrElse(key, Set()).contains(value)
      } yield {
        DependencyGenerator(key, value)
      }
      val generatorsInCycle: Option[CycleGenerator] =
        if (cycles.contains(key) && !cycles(key).exists(soFar.alreadyProcessed.contains)) {
          val cycle = cycles(key)
          val sortedCycleKeys = cycle.toSeq.sorted
          val parts = for {
            cycleKey <- sortedCycleKeys
            values = dependsOn(cycleKey)
            value <- values.toSeq.sorted
            if cycles(key).contains(value)
          } yield {
            (cycleKey, value)
          }
          Some(CycleGenerator(sortedCycleKeys.head, parts))
        } else {
          None
        }
      val newGenerators = soFar.generators ++ generators ++ generatorsInCycle.toSeq
      val newAlreadyProcessed = soFar.alreadyProcessed + key
      Accumulator(newAlreadyProcessed, newGenerators)
    }
    val accumulator = sortedKeys.foldLeft(Accumulator.Empty)(addDependency)
    val generators = Seq(HeaderGenerator) ++ singleKeys ++ accumulator.generators ++ Seq(FooterGenerator)
    val lines = generators.flatMap(_.generate)
    lines
  }

  sealed trait LinesGenerator {
    def generate: Seq[String]
  }

  case class Accumulator(alreadyProcessed: Set[String], generators: Seq[LinesGenerator])

  case class SingleGenerator(key: String) extends LinesGenerator {
    override def generate: Seq[String] = Seq( s"""  "$key";""")
  }

  case class DependencyGenerator(key: String, value: String) extends LinesGenerator {
    override def generate: Seq[String] = Seq( s"""  "$key" -> "$value";""")
  }

  case class CycleGenerator(name: String, parts: Seq[(String, String)]) extends LinesGenerator {
    override def generate: Seq[String] = {
      val dependencies = for {
        (key, value) <- parts
      } yield {
        s"""    "$key" -> "$value";"""
      }
      Seq(s"  subgraph cluster_$name {",
        "    penwidth=2;",
        "    pencolor=Red;") ++ dependencies ++
        Seq("  }")
    }
  }

  object Accumulator {
    val Empty = Accumulator(Set(), Seq())
  }

  object HeaderGenerator extends LinesGenerator {
    override def generate: Seq[String] = Seq("digraph detangled {")
  }

  object FooterGenerator extends LinesGenerator {
    override def generate: Seq[String] = Seq("}")
  }

}
