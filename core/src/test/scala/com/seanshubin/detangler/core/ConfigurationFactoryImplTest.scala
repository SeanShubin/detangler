package com.seanshubin.detangler.core

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.devon.core.devon.{DevonMarshaller, DevonMarshallerWiring}
import org.scalatest.FunSuite

class ConfigurationFactoryImplTest extends FunSuite {
  val configFileName: String = "environment.txt"
  val args = Seq(configFileName)
  val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  val charset: Charset = StandardCharsets.UTF_8

  test("complete configuration") {
    val content =
      """{
        |  reportDir report-dir
        |  searchPaths
        |  [
        |    search-path-1
        |    search-path-2
        |  ]
        |  level 3
        |  startsWith
        |  {
        |    include [
        |      [ com seanshubin ]
        |      [ seanshubin ]
        |    ]
        |    drop [
        |      [ com seanshubin ]
        |      [ seanshubin ]
        |    ]
        |  }
        |}""".stripMargin
    val expected = Right(Configuration.Sample)
    val filesStub = new FilesStub(Map("environment.txt" -> content), charset)
    val configurationFactory = new ConfigurationFactoryImpl(filesStub, devonMarshaller, charset)
    val actual = configurationFactory.validate(args)
    assert(actual === expected)
  }

  test("missing configuration file") {
    val content =
      """{
        |  servePathOverride gui/src/main/resources/
        |  optionalPathPrefix /template
        |}
        | """.stripMargin

    val expected = Left(Seq("Configuration file named 'environment.txt' not found"))
    val filesStub = new FilesStub(Map(), charset)
    val configurationFactory = new ConfigurationFactoryImpl(filesStub, devonMarshaller, charset)
    val actual = configurationFactory.validate(args)
    assert(actual === expected)
  }

  test("malformed configuration") {
    val content = "{"
    val expected = Left(Seq("There was a problem reading the configuration file 'environment.txt': Could not match 'element', expected one of: map, array, string, null"))
    val filesStub = new FilesStub(Map("environment.txt" -> content), charset)
    val configurationFactory = new ConfigurationFactoryImpl(filesStub, devonMarshaller, charset)
    val actual = configurationFactory.validate(args)
    assert(actual === expected)
  }
}
