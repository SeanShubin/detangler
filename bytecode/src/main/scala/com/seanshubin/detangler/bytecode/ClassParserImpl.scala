package com.seanshubin.detangler.bytecode

import java.io.DataInput

class ClassParserImpl extends ClassParser {
  def parseClassDependencies(dataInput: DataInput): (String, Seq[String]) = {
    val classFileInfo = ClassFileInfo.fromDataInput(dataInput)
    val thisClassName = classFileInfo.thisClassName
    val dependencyNames = classFileInfo.dependencyNames
    val dependencies = (thisClassName, dependencyNames)
    dependencies
  }
}
