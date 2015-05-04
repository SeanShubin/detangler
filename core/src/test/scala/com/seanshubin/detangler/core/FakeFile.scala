package com.seanshubin.detangler.core

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}
import java.nio.charset.Charset

class FakeFile(text: String, charset: Charset) {
  def createOutputStream(): OutputStream = new ByteArrayOutputStream()

  def createInputStream(): InputStream = new ByteArrayInputStream(text.getBytes(charset))
}
