package com.seanshubin.detangler.domain

class RunnableStub extends Runnable {
  var runCallCount = 0

  override def run(): Unit = runCallCount += 1
}
