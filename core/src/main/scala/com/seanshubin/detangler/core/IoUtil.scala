package com.seanshubin.detangler.core

import java.io._
import java.nio.charset.Charset

import scala.annotation.tailrec

object IoUtil {
  @tailrec final def copyInputStreamToOutputStream(in: InputStream, out: OutputStream): Unit = {
    val ch = in.read()
    if (ch != -1) {
      out.write(ch)
      copyInputStreamToOutputStream(in, out)
    }
  }

  def inputStreamToString(inputStream: InputStream, charset: Charset): String = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    copyInputStreamToOutputStream(inputStream, byteArrayOutputStream)
    new String(byteArrayOutputStream.toByteArray, charset)
  }
}
