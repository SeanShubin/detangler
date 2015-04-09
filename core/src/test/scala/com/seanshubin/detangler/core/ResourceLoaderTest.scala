package com.seanshubin.detangler.core

import java.io.ByteArrayInputStream

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ResourceLoaderTest extends FunSuite with EasyMockSugar {
  test("input stream for resource") {
    val fileName: String = "some-file.txt"
    val expectedInputStream = new ByteArrayInputStream(Array[Byte](1, 2, 3))
    val classLoaderIntegration = mock[ClassLoaderIntegration]
    val resourceLoader: ResourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
    expecting {
      classLoaderIntegration.getResourceAsStream(fileName).andReturn(expectedInputStream)
    }
    whenExecuting(classLoaderIntegration) {
      val actualInputStream = resourceLoader.inputStreamFor(fileName)
      assert(actualInputStream === expectedInputStream)
    }
  }

  test("sensible error message when can't find resource") {
    val fileName: String = "some-file.txt"
    val classLoaderIntegration = mock[ClassLoaderIntegration]
    val resourceLoader: ResourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
    expecting {
      classLoaderIntegration.getResourceAsStream(fileName).andReturn(null)
    }
    whenExecuting(classLoaderIntegration) {
      val actualException = intercept[RuntimeException] {
        resourceLoader.inputStreamFor(fileName)
      }
      assert(actualException.getMessage === "Could not load resource named 'some-file.txt'")
    }
  }
}

