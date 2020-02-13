package com.seanshubin.detangler.domain

import scala.collection.mutable.ArrayBuffer

class NotificationsStub extends NotificationsNotImplemented {
  val invocations = new ArrayBuffer[AnyRef]

  override def configurationError(lines: Seq[String]): Unit = {
    invocations.append(("configurationError", lines))
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    invocations.append(("effectiveConfiguration", configuration))
  }
}
