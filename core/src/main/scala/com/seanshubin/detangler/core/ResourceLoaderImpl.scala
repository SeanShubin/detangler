package com.seanshubin.detangler.core

import java.io.InputStream

class ResourceLoaderImpl(classLoaderIntegration: ClassLoaderIntegration) extends ResourceLoader {
  override def inputStreamFor(name: String): InputStream = {
    val inputStream = classLoaderIntegration.getResourceAsStream(name)
    if (inputStream == null) {
      throw new RuntimeException(s"Could not load resource named '$name'")
    } else {
      inputStream
    }
  }
}
