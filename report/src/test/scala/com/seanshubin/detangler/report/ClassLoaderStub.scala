package com.seanshubin.detangler.report

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.Charset

import com.seanshubin.detangler.contract.test.ClassLoaderNotImplemented

class ClassLoaderStub(resourceMap: Map[String, String], charset: Charset) extends ClassLoaderNotImplemented {
  override def getResourceAsStream(name: String): InputStream = {
    val bytes = resourceMap(name).getBytes(charset)
    new ByteArrayInputStream(bytes)
  }
}
