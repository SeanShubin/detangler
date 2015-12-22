package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Detangled

class DetanglerImpl(cycleFinder: CycleFinder) extends Detangler {
  override def analyze(data: Seq[(String, String)]): Detangled = {
    val dependsOnMap = data.foldLeft(Map[String, Set[String]]())(CollectionUtil.appendPairToMapFromKeyToSetOfValues)
    analyze(dependsOnMap)
  }

  def analyze(dependsOnMap: Map[String, Set[String]]): Detangled = {
    val dependedOnByMap = CollectionUtil.invert(dependsOnMap)
    val cycles: Set[Set[String]] = cycleFinder.findCycles(dependsOnMap)
    println(("-" * 40) + "DEPENDS ON" + ("-" * 40))
    println(dependsOnMap)
    println(("-" * 40) + "CYCLES" + ("-" * 40))
    println(cycles)
    println(("-" * 40) + "DEPENDED ON BY" + ("-" * 40))
    println(dependedOnByMap)
    ???
  }
}
