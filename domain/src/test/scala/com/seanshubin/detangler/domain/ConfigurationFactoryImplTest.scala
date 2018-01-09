package com.seanshubin.detangler.domain

import java.nio.charset.{Charset, StandardCharsets}

import com.seanshubin.devon.domain.{DevonMarshaller, DevonMarshallerWiring}
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
        |  searchPaths [ search-path-1 search-path-2 ]
        |  level 3
        |  startsWith
        |  {
        |    include
        |    [
        |      [ com seanshubin ]
        |      [ seanshubin ]
        |    ]
        |    exclude
        |    [
        |      [ com seanshubin unchecked ]
        |    ]
        |    drop
        |    [
        |      [ com seanshubin ]
        |      [ seanshubin ]
        |    ]
        |  }
        |  ignoreFiles [ ignore-file.jar ]
        |  canFailBuild true
        |  allowedInCycle detangler-allowed-in-cycle.txt
        |}
        | """.stripMargin
    val detanglerAllowedInCycleContent =
      """[ branch ]
        |[ tree ]
        |[ leaf ]
        |""".stripMargin

    val expected = Right((Configuration.Sample, Configuration.SampleAllowedInCycles))
    val filesStub = new FilesStub(Map(
      "environment.txt" -> content,
      "detangler-allowed-in-cycle.txt" -> detanglerAllowedInCycleContent), charset)
    val configurationFactory = new ConfigurationFactoryImpl(filesStub, devonMarshaller, charset)
    val actual = configurationFactory.validate(args)
    assert(actual === expected)
  }

  test("missing configuration file") {
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
