package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class JsoupApiTest extends FunSuite {
  test("compose html") {
    val jsoup: JsoupApi = new JsoupApiImpl
    val fragmentA = jsoup.parse("<p>hello</p>")
    val fragmentB = jsoup.parse("<p>world</p>")
    val fragmentC = jsoup.concat(fragmentA, fragmentB)
    assert(fragmentC.asText === "<p>hello</p>\n<p>world</p>")
  }
}
