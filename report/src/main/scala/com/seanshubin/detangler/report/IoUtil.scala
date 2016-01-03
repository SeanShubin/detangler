package com.seanshubin.detangler.report

import java.io._
import java.nio.charset.Charset

import scala.annotation.tailrec

object IoUtil {
  @tailrec
  def copy(inputStream: InputStream, outputStream: OutputStream): Unit = {
    val ch = inputStream.read()
    if (ch != -1) {
      outputStream.write(ch)
      copy(inputStream, outputStream)
    }
  }

  def toInputStream(text: String, charset: Charset): InputStream = {
    val bytes = text.getBytes(charset)
    new ByteArrayInputStream(bytes)
  }

  def copy(inputStream: InputStream, printStream: PrintStream, charset: Charset): Unit = {
    val reader = new BufferedReader(new InputStreamReader(inputStream, charset))
    copy(reader, printStream)
  }

  @tailrec
  def copy(bufferedReader: BufferedReader, printStream: PrintStream): Unit = {
    val line = bufferedReader.readLine()
    if (line != null) {
      printStream.println(line)
      copy(bufferedReader, printStream)
    }
  }
}
