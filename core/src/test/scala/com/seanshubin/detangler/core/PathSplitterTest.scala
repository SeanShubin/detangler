package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class PathSplitterTest extends FunSuite {
  test("split path") {
    val s1 = "com/seanshubin/devon/core/devon/DevonMarshaller$foo"
    val s2 = "com/seanshubin/utility/filesystem/FileSystemIntegration$bar"
    val pattern = """com/seanshubin/([^/]+)/(.*)/([^$]+).*"""
    val pathSplitter = new PathSplitter(pattern)
    assert(pathSplitter.split(s1) === Seq("devon", "core/devon", "DevonMarshaller"))
    assert(pathSplitter.split(s2) === Seq("utility", "filesystem", "FileSystemIntegration"))
  }
}
