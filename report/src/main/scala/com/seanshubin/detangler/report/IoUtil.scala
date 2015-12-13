package com.seanshubin.detangler.report

import java.io.{ByteArrayInputStream, InputStream, OutputStream}
import java.nio.charset.Charset

import scala.annotation.tailrec

object IoUtil {
  @tailrec
  def copyInputStreamToOutputStream(inputStream: InputStream, outputStream: OutputStream): Unit = {
    val ch = inputStream.read()
    if (ch != -1) {
      outputStream.write(ch)
      copyInputStreamToOutputStream(inputStream, outputStream)
    }
  }

  def stringToInputStream(text: String, charset: Charset): InputStream = {
    val bytes = text.getBytes(charset)
    new ByteArrayInputStream(bytes)
  }
}
