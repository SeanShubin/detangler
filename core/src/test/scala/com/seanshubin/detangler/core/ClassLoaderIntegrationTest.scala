package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class ClassLoaderIntegrationTest extends FunSuite {
  test("load resource") {
    val charset = StandardCharsets.UTF_8
    val expectedString = "Hello class loader integration test!"
    val expectedBytes = expectedString.getBytes(charset)
    val actualBytes = new Array[Byte](expectedBytes.size)
    val classLoader = this.getClass.getClassLoader
    val classLoaderIntegration = new ClassLoaderIntegrationImpl(classLoader)
    val inputStream = classLoaderIntegration.getResourceAsStream("for-class-loader-integration-test.txt")
    inputStream.read(actualBytes)
    assert(actualBytes === expectedBytes)
  }
}
