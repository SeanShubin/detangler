package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone

class AcceptNameFunction(stringToStandaloneFunction: String => Option[Standalone]) extends (String => Boolean) {
  override def apply(name: String): Boolean = {
    val classSuffix = ".class"
    if (name.endsWith(classSuffix)) {
      stringToStandaloneFunction(name.take(name.length - ".class".length)).isDefined
    } else {
      false
    }
  }
}
