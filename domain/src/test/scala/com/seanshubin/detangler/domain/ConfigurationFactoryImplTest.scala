package com.seanshubin.detangler.domain

import java.lang
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.attribute.FileAttribute
import java.nio.file.{LinkOption, OpenOption, Path, Paths}

import com.seanshubin.detangler.contract.test.FilesNotImplemented
import com.seanshubin.devon.domain.{Devon, DevonMarshaller, DevonMarshallerWiring}
import org.scalatest.FunSuite

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

class ConfigurationFactoryImplTest extends FunSuite {
  val configFileName: String = "detangler.txt"
  val args = Seq(configFileName)
  val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  val charset: Charset = StandardCharsets.UTF_8
  val sampleConfigDevon: Devon = devonMarshaller.fromValue(Configuration.Sample)
  val prettySampleLines: Seq[String] = devonMarshaller.toPretty(sampleConfigDevon)

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
      "detangler.txt" -> content,
      "detangler-allowed-in-cycle.txt" -> detanglerAllowedInCycleContent), charset)
    val configurationFactory = new ConfigurationFactoryImpl(filesStub, devonMarshaller, charset)
    val actual = configurationFactory.validate(args)
    assert(actual === expected)
  }

  test("missing configuration file") {
    // given
    val allowedCycles = Seq[Seq[String]]()
    val expected = Right((Configuration.Default, allowedCycles))
    val createFileInvocations = ArrayBuffer[Path]()
    val writeInvocations = ArrayBuffer[(Path, lang.Iterable[_ <: CharSequence])]()
    val sampleLines = devonMarshaller.valueToPretty(Configuration.Default)
    val sampleBytes = sampleLines.mkString("\n").getBytes(charset).toSeq
    val readAllBytesMap = Map[Path, Seq[Byte]](
      Paths.get("detangler.txt") -> sampleBytes,
      Paths.get("detangler-allowed-in-cycle.txt") -> Seq(),
    )
    val files = new FilesNotImplemented {
      override def exists(path: Path, options: LinkOption*): Boolean = false

      override def createFile(path: Path, attrs: FileAttribute[_]*): Path = {
        createFileInvocations.append(path)
        path
      }

      override def write(path: Path, lines: lang.Iterable[_ <: CharSequence], options: OpenOption*): Path = {
        writeInvocations.append((path, lines))
        path
      }

      override def readAllBytes(path: Path): Seq[Byte] = {
        readAllBytesMap(path)
      }
    }
    val configurationFactory = new ConfigurationFactoryImpl(files, devonMarshaller, charset)

    // when
    val actual = configurationFactory.validate(args)

    // then
    assert(createFileInvocations.size === 2)
    assert(createFileInvocations(0) === Paths.get("detangler.txt"))
    assert(createFileInvocations(1) === Paths.get("detangler-allowed-in-cycle.txt"))
    assert(writeInvocations.size === 1)
    assert(writeInvocations(0)._1 === Paths.get("detangler.txt"))
    assert(writeInvocations(0)._2.asScala.toSeq === sampleLines)
    assert(actual === expected)
  }

  test("malformed configuration") {
    val content = "{"
    val expected = Left(Seq(
      "There was a problem reading the configuration file 'detangler.txt': Could not match 'element', expected one of: map, array, string, null",
      "A typical configuration file might look something like this",
      "") ++ prettySampleLines)
    val filesStub = new FilesStub(Map("detangler.txt" -> content), charset)
    val configurationFactory = new ConfigurationFactoryImpl(filesStub, devonMarshaller, charset)
    val actual = configurationFactory.validate(args)
    assert(actual === expected)
  }
}
