package com.seanshubin.detangler.scanner

import java.io.{ByteArrayInputStream, DataInputStream}

import com.seanshubin.detangler.bytecode.ClassParser

class ClassBytesScannerImpl(classParser: ClassParser) extends ClassBytesScanner {
  override def parseDependencies(classBytes: Seq[Byte]): (String, Seq[String]) = {
    val byteArrayInputStream = new ByteArrayInputStream(classBytes.toArray)
    val dataInput = new DataInputStream(byteArrayInputStream)
    classParser.parseClassDependencies(dataInput)
  }
}
