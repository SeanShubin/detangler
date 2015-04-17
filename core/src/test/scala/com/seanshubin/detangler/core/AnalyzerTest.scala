package com.seanshubin.detangler.core

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class AnalyzerTest extends FunSuite with EasyMockSugar {
  test("application flow") {
    val lines = new ArrayBuffer[String]()
    val emitLine: String => Unit = line => lines.append(line)
    val runner: Analyzer = new AnalyzerImpl("world", emitLine)
    runner.run()
    assert(lines.size === 1)
    assert(lines(0) === "Hello, world!")
  }
}
