package com.seanshubin.detangler.core

import java.io.InputStream

trait ClassLoaderIntegration {
  def getResourceAsStream(name: String): InputStream
}
