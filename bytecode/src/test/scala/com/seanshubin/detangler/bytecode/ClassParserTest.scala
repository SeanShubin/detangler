package com.seanshubin.detangler.bytecode

import java.io.DataInputStream

import com.seanshubin.detangler.collection.SetDifference
import org.scalatest.FunSuite

class ClassParserTest extends FunSuite {
  test("parse simple class") {
    val classParser = new ClassParserImpl()
    val className = "com/seanshubin/detangler/bytecode/ClassParserImpl.class"
    val inputStream = getClass.getClassLoader.getResourceAsStream(className)
    if (inputStream == null) {
      throw new RuntimeException(s"not found: $className")
    }
    val parsedClass = try {
      val dataInput = new DataInputStream(inputStream)
      classParser.parseClassDependencies(dataInput)
    } finally {
      inputStream.close()
    }
    val expectedClass = "com/seanshubin/detangler/bytecode/ClassParserImpl"
    val expectedDependencies = Set(
      "java/lang/Object",
      "com/seanshubin/detangler/bytecode/ClassFileInfo",
      "com/seanshubin/detangler/bytecode/ClassParser",
      "scala/Tuple2",
      "com/seanshubin/detangler/bytecode/ClassFileInfo$")
    val (actualClass, actualDependencies) = parsedClass
    assert(actualClass === expectedClass)
    val diff = SetDifference.diff(actualDependencies.toSet, expectedDependencies)
    assert(diff.isSame, diff.messageLines.mkString("\n"))
  }
}
