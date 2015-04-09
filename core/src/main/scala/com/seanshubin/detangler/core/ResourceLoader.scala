package com.seanshubin.detangler.core

import java.io.InputStream

trait ResourceLoader {
  def inputStreamFor(name: String): InputStream
}
