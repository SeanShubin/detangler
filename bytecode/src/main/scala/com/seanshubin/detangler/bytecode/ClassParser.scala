package com.seanshubin.detangler.bytecode

import java.io.DataInput

trait ClassParser {
  def parseClassDependencies(dataInput: DataInput): (String, Seq[String])
}
