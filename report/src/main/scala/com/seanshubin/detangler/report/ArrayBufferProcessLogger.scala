package com.seanshubin.detangler.report

import scala.collection.mutable.ArrayBuffer
import scala.sys.process.ProcessLogger

class ArrayBufferProcessLogger extends ProcessLogger {
  val outputBuffer = new ArrayBuffer[String]()
  val errorBuffer = new ArrayBuffer[String]()

  override def out(s: => String): Unit = {
    outputBuffer.append(s)
  }

  override def err(s: => String): Unit = {
    errorBuffer.append(s)
  }

  override def buffer[T](f: => T): T = f
}
