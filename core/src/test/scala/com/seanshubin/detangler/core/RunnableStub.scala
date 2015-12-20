package com.seanshubin.detangler.core

class RunnableStub extends Runnable {
  var runCallCount = 0

  override def run(): Unit = runCallCount += 1
}
