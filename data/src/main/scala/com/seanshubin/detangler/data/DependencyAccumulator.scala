package com.seanshubin.detangler.data

case class DependencyAccumulator(dependencies: Map[Seq[String], Set[Seq[String]]]) {
  def addValues(target: Seq[String], dependsOn: Seq[Seq[String]]): DependencyAccumulator = {
    if (dependencies.contains(target)) {
      def addParameterValue(soFar: DependencyAccumulator, parameterValue: Seq[String]) = soFar.addValue(target, parameterValue)
      dependsOn.foldLeft(this)(addParameterValue)
    } else {
      addKey(target).addValues(target, dependsOn)
    }
  }

  def transpose(): DependencyAccumulator = {
    def transposeAndAddValues(accumulator: DependencyAccumulator, entry: (Seq[String], Set[Seq[String]])): DependencyAccumulator = {
      val (key, values) = entry
      def transposeAndAddValue(accumulator: DependencyAccumulator, value: Seq[String]): DependencyAccumulator = {
        accumulator.addValue(value, key)
      }
      values.foldLeft(accumulator)(transposeAndAddValue)
    }
    dependencies.foldLeft(keysOnly)(transposeAndAddValues)
  }

  private def keysOnly: DependencyAccumulator = DependencyAccumulator(dependencies.keys.map((_, Set[Seq[String]]())).toMap)

  private def addValue(target: Seq[String], dependsOn: Seq[String]): DependencyAccumulator = {
    if (dependencies.contains(dependsOn)) {
      if (target == dependsOn) {
        this
      } else {
        DependencyAccumulator(dependencies.updated(target, dependencies(target) + dependsOn))
      }
    } else {
      addKey(dependsOn).addValue(target, dependsOn)
    }
  }

  private def addKey(target: Seq[String]): DependencyAccumulator = {
    DependencyAccumulator(dependencies.updated(target, dependencies.getOrElse(target, Set())))
  }
}

object DependencyAccumulator {
  val Empty = new DependencyAccumulator(Map())

  def fromIterable(iterable: Iterable[(Seq[String], Seq[Seq[String]])]): DependencyAccumulator = {
    def addEntry(accumulator: DependencyAccumulator, entry: (Seq[String], Seq[Seq[String]])): DependencyAccumulator = {
      val (key, values) = entry
      accumulator.addValues(key, values)
    }
    iterable.foldLeft(Empty)(addEntry)
  }
}
