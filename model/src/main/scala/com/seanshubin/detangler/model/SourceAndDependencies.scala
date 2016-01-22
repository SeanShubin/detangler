package com.seanshubin.detangler.model

import com.seanshubin.detangler.collection.MapUtilities

case class SourceAndDependencies(sourceName:String, dependencies:Map[Standalone, Set[Standalone]]){
  def merge(that:SourceAndDependencies):SourceAndDependencies = {
    val newDependencies = MapUtilities.merge(this.dependencies, that.dependencies)
    copy(dependencies = newDependencies)
  }
}

object SourceAndDependencies {
  def merge(left:SourceAndDependencies, right:SourceAndDependencies):SourceAndDependencies = left.merge(right)
}