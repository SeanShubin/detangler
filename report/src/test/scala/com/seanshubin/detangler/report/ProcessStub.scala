package com.seanshubin.detangler.report

import java.io.{InputStream, OutputStream}

class ProcessStub extends Process {
  override def exitValue(): Int = ???

  override def destroy(): Unit = ???

  override def waitFor(): Int = ???

  override def getOutputStream: OutputStream = ???

  override def getErrorStream: InputStream = ???

  override def getInputStream: InputStream = ???
}
