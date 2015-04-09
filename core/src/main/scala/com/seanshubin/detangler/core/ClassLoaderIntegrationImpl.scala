package com.seanshubin.detangler.core

import java.io.InputStream

class ClassLoaderIntegrationImpl(classLoader: ClassLoader) extends ClassLoaderIntegration {
  override def getResourceAsStream(name: String): InputStream = classLoader.getResourceAsStream(name)
}
