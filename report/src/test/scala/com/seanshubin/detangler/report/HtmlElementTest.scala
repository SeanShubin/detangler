package com.seanshubin.detangler.report

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class HtmlElementTest extends FunSuite {
  val samplePage =
    """<html>
      |<body>
      |<h1>Hello, world!</h1>
      |</body>
      |</html>""".stripMargin
  val sampleFragment = "<h1>Hello, world!</h1>"
  val charset = StandardCharsets.UTF_8
  test("compose from input stream") {
    val element = HtmlElement.fromInputStream(IoUtil.stringToInputStream(samplePage, charset), charset)
    println(element.text)
    println(element.select("body h1").text)
  }
}
