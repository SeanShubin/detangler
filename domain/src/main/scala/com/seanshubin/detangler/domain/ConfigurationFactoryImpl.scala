package com.seanshubin.detangler.domain

import java.nio.charset.Charset
import java.nio.file.Paths

import com.seanshubin.detangler.contract.FilesContract
import com.seanshubin.devon.domain.DevonMarshaller

class ConfigurationFactoryImpl(files: FilesContract,
                               devonMarshaller: DevonMarshaller,
                               charset: Charset) extends ConfigurationFactory {
  override def validate(args: Seq[String]): Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    if (args.length == 1) {
      val configFilePath = Paths.get(args(0))
      try {
        if (files.exists(configFilePath)) {
          val bytes = files.readAllBytes(configFilePath)
          val text = new String(bytes.toArray, charset)
          val devon = devonMarshaller.fromString(text)
          val configWithNulls = devonMarshaller.toValue(devon, classOf[Configuration])
          val config = configWithNulls.replaceNullsWithDefaults()
          validateAllowedCycles(config)
        } else {
          createError(s"Configuration file named '$configFilePath' not found")
        }
      } catch {
        case ex: Throwable =>
          createError(s"There was a problem reading the configuration file '$configFilePath': ${ex.getMessage}")
      }
    } else {
      createError("Expected exactly one argument, the name of the configuration file")
    }
  }

  private def validateAllowedCycles(configuration: Configuration): Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    val path = configuration.allowedInCycle
    if (path == null) {
      Right(configuration, Seq())
    } else {
      if (files.exists(configuration.allowedInCycle)) {
        val bytes = files.readAllBytes(path)
        val text = new String(bytes.toArray, charset)
        val iterator = devonMarshaller.stringToIterator(text)
        val allowedCycles = iterator.map(devon => devonMarshaller.toValue(devon, classOf[Seq[String]]))
        Right((configuration, allowedCycles.toSeq))
      } else {
        Left(Seq(s"File for allowed cycles '$path' does not exist"))
      }
    }
  }

  private def createError(message:String):Either[Seq[String], (Configuration, Seq[Seq[String]])] = {
    val sampleConfigDevon = devonMarshaller.fromValue(Configuration.Sample)
    val prettySampleLines = devonMarshaller.toPretty(sampleConfigDevon)
    Left(Seq(
      message,
      "A typical configuration file might look something like this",
      "") ++ prettySampleLines)
  }
}
