package com.seanshubin.detangler.bytecode

import java.io.DataInputStream
import java.nio.file.{Files, Paths}

import org.scalatest.FunSuite

class ClassParserTest extends FunSuite {
  test("parse simple class") {
    val classFile = "sample-data/Foo.class"
    val path = Paths.get(classFile)
    val classParser = new ClassParserImpl()
    val inputStream = Files.newInputStream(path)
    val parsedClass = try {
      val dataInput = new DataInputStream(inputStream)
      classParser.parseClassDependencies(dataInput)
    } finally {
      inputStream.close()
    }
    val expectedClass: String = "com/seanshubin/dependency/utility/class_format/Foo"
    val expectedDependencies: Set[String] = Set("java/lang/Object", "java/lang/System", "java/io/PrintStream")
    assert(parsedClass ===(expectedClass, expectedDependencies))
  }

}
