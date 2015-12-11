package com.seanshubin.detangler.model

import com.seanshubin.detangler.modle.Module
import org.scalatest.FunSuite

class ModuleTest extends FunSuite {
  test("simple module") {
    val simple = Module(Seq("a", "c", "f"))
    assert(simple.id === "a-c-f")
  }
}
