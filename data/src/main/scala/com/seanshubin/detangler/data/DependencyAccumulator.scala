package com.seanshubin.detangler.data

case class DependencyAccumulator[T](dependencies: Map[T, Set[T]]) {
  def addValues(target: T, dependsOn: Set[T]): DependencyAccumulator[T] = {
    if (dependencies.contains(target)) {
      def addParameterValue(soFar: DependencyAccumulator[T], parameterValue: T) = soFar.addValue(target, parameterValue)
      dependsOn.foldLeft(this)(addParameterValue)
    } else {
      addKey(target).addValues(target, dependsOn)
    }
  }

  def addValues(map:Map[T, Set[T]]): DependencyAccumulator[T] = {
    def addMapEntry(accumulator: DependencyAccumulator[T], entry:(T, Set[T])):DependencyAccumulator[T] ={
      val (target, dependsOn) = entry
      accumulator.addValues(target, dependsOn)
    }
    map.toSeq.foldLeft(this)(addMapEntry)
  }

  def transpose(): DependencyAccumulator[T] = {
    def transposeAndAddValues(accumulator: DependencyAccumulator[T], entry: (T, Set[T])): DependencyAccumulator[T] = {
      val (key, values) = entry
      def transposeAndAddValue(accumulator: DependencyAccumulator[T], value: T): DependencyAccumulator[T] = {
        accumulator.addValue(value, key)
      }
      values.foldLeft(accumulator)(transposeAndAddValue)
    }
    dependencies.foldLeft(keysOnly)(transposeAndAddValues)
  }

  private def keysOnly: DependencyAccumulator[T] = DependencyAccumulator[T](dependencies.keys.map((_, Set[T]())).toMap)

  private def addValue(target: T, dependsOn: T): DependencyAccumulator[T] = {
    if (dependencies.contains(dependsOn)) {
      if (target == dependsOn) {
        this
      } else {
        DependencyAccumulator[T](dependencies.updated(target, dependencies(target) + dependsOn))
      }
    } else {
      addKey(dependsOn).addValue(target, dependsOn)
    }
  }

  private def addKey(target: T): DependencyAccumulator[T] = {
    DependencyAccumulator[T](dependencies.updated(target, dependencies.getOrElse(target, Set())))
  }
}

object DependencyAccumulator {
  def empty[T](): DependencyAccumulator[T] = {
    val emptyMap: Map[T, Set[T]] = Map()
    val empty: DependencyAccumulator[T] = DependencyAccumulator(emptyMap)
    empty
  }

  def fromIterable[T](iterable: Iterable[Map[T, Set[T]]]): DependencyAccumulator[T] = {
    def addMap(accumulator: DependencyAccumulator[T], map: Map[T, Set[T]]): DependencyAccumulator[T] = {
      accumulator.addValues(map)
    }
    val emptyMap: Map[T, Set[T]] = Map()
    val empty: DependencyAccumulator[T] = DependencyAccumulator(emptyMap)
    iterable.foldLeft(empty)(addMap)
  }
}
