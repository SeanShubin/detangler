package com.seanshubin.detangler.core

import scala.collection.mutable.ArrayBuffer

class NotificationsStub extends Notifications {
  val invocations = new ArrayBuffer[AnyRef]

  override def configurationError(lines: Seq[String]): Unit = {
    invocations.append(("configurationError", lines))
  }

  override def effectiveConfiguration(configuration: Configuration): Unit = {
    invocations.append(("effectiveConfiguration", configuration))
  }

  override def topLevelException(exception: Throwable): Unit = ???
}
