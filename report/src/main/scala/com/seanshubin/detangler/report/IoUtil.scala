package com.seanshubin.detangler.report

import java.io.{InputStream, OutputStream}

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
}
