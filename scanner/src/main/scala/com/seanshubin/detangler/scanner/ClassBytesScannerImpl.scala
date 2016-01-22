package com.seanshubin.detangler.scanner

import java.io.{ByteArrayInputStream, DataInputStream}

import com.seanshubin.detangler.bytecode.ClassParser

class ClassBytesScannerImpl(classParser: ClassParser) extends ClassBytesScanner {
  override def parseDependencies(scannedBytes: ScannedBytes): ScannedDependencies = {
    val byteArrayInputStream = new ByteArrayInputStream(scannedBytes.bytes.toArray)
    val dataInput = new DataInputStream(byteArrayInputStream)
    val (name, dependencies) = classParser.parseClassDependencies(dataInput)
    val sourceName = scannedBytes.sourceName
    ScannedDependencies(sourceName, name , dependencies)
  }
}
