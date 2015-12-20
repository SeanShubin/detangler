package com.seanshubin.detangler.core

import java.nio.charset.Charset
import java.nio.file.{LinkOption, Path}

import com.seanshubin.detangler.contract.FilesNotImplemented

class FilesStub(fileContentByName: Map[String, String], charset: Charset) extends FilesNotImplemented {
  override def exists(path: Path, options: LinkOption*): Boolean = {
    fileContentByName.contains(path.toString)
  }

  override def readAllBytes(path: Path): Seq[Byte] = {
    val stringContent = fileContentByName(path.toString)
    val bytes = stringContent.getBytes(charset)
    bytes.toSeq
  }
}
