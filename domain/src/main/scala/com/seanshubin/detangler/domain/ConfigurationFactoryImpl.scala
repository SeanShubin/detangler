package com.seanshubin.detangler.domain

import java.nio.charset.Charset
import java.nio.file.{Path, Paths}

import com.seanshubin.detangler.contract.FilesContract
import com.seanshubin.devon.domain.DevonMarshaller

import scala.collection.JavaConverters._

class ConfigurationFactoryImpl(files: FilesContract,
                               devonMarshaller: DevonMarshaller,
                               charset: Charset) extends ConfigurationFactory {
  override def validate(args: Seq[String]): Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    val configFileName = if (args.length < 1) "detangler.txt" else args(0)
    val configFilePath = Paths.get(configFileName)
    try {
      handleMissingConfiguration(configFilePath)
      val bytes = files.readAllBytes(configFilePath)
      val text = new String(bytes.toArray, charset)
      val devon = devonMarshaller.fromString(text)
      val configWithPossiblyEmpty = devonMarshaller.toValue(devon, classOf[RawConfiguration])
      val config = configWithPossiblyEmpty.replaceEmptyWithDefaults(configFilePath)
      validateAllowedCycles(config)
    } catch {
      case ex: Throwable =>
        createError(s"There was a problem reading the configuration file '$configFilePath': ${ex.getMessage}")
    }
  }

  private def handleMissingConfiguration(configFilePath: Path) = {
    if (!files.exists(configFilePath)) {
      files.createFile(configFilePath)
      val configuration = Configuration.Default
      val lines = devonMarshaller.valueToPretty(configuration)
      files.write(configFilePath, lines.asJava)
    }
  }

  private def validateAllowedCycles(configuration: Configuration): Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    val path = configuration.allowedInCycle
    if (path == null) {
      Right(configuration, Seq())
    } else {
      if (!files.exists(configuration.allowedInCycle)) {
        files.createFile(configuration.allowedInCycle)
      }
      val bytes = files.readAllBytes(path)
      val text = new String(bytes.toArray, charset)
      val iterator = devonMarshaller.stringToIterator(text)
      val allowedCycles = iterator.map(devon => devonMarshaller.toValue(devon, classOf[Seq[String]]))
      Right((configuration, allowedCycles.toSeq))
    }
  }

  private def createError(message: String): Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    val sampleConfigDevon = devonMarshaller.fromValue(Configuration.Sample)
    val prettySampleLines = devonMarshaller.toPretty(sampleConfigDevon)
    Left(Seq(
      message,
      "A typical configuration file might look something like this",
      "") ++ prettySampleLines)
  }
}
