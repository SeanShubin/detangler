package com.seanshubin.detangler.scanner

case class ScannedDependencies(sourceName:String, thisDependency:String, dependsOn:Seq[String])
