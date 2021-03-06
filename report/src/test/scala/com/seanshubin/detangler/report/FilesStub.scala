package com.seanshubin.detangler.report

import java.io.{ByteArrayOutputStream, OutputStream}
import java.lang.Iterable
import java.nio.charset.Charset
import java.nio.file.attribute.FileAttribute
import java.nio.file.{OpenOption, Path}

import com.seanshubin.detangler.contract.test.FilesNotImplemented

class FilesStub(charset: Charset) extends FilesNotImplemented {
  var writeResults: Map[String, ByteArrayOutputStream] = Map()
  var directoriesCreated: Seq[Path] = Seq()

  def stringContentsOf(fileName: String): String = {
    val outputStream = writeResults(fileName)
    val bytes = outputStream.toByteArray
    val text = new String(bytes, charset)
    text
  }

  def fileNames(): Set[String] = {
    writeResults.keySet
  }

  override def write(path: Path, bytes: Seq[Byte], options: OpenOption*): Path = {
    this.synchronized {
      val outputStream = newOutputStream(path, options: _*)
      outputStream.write(bytes.toArray)
      path
    }
  }

  override def newOutputStream(path: Path, options: OpenOption*): OutputStream = {
    this.synchronized {
      val outputStream = new ByteArrayOutputStream()
      writeResults = writeResults + (path.getFileName.toString -> outputStream)
      outputStream
    }
  }

  override def write(path: Path, lines: Iterable[_ <: CharSequence], cs: Charset, options: OpenOption*): Path = {
    path
  }

  override def createDirectories(dir: Path, attrs: FileAttribute[_]*): Path = {
    directoriesCreated = directoriesCreated :+ dir
    dir
  }
}
