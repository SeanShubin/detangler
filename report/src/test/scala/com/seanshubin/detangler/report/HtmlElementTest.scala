package com.seanshubin.detangler.report

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class HtmlElementTest extends FunSuite {
  val sampleRawPage =
    """<html>
      |<body>
      |<h1>Hello, world!</h1>
      |</body>
      |</html>""".stripMargin
  val sampleGeneratedPage =
    """<html>
      |<head></head>
      |<body>
      |<h1>Hello, world!</h1>
      |</body>
      |</html>""".stripMargin
  val sampleFragment = "<h1>Hello, world!</h1>"
  val charset = StandardCharsets.UTF_8

  test("page from input stream") {
    val element = HtmlElement.pageFromInputStream(IoUtil.stringToInputStream(sampleRawPage, charset), charset)
    assertLinesSame(element.text, sampleGeneratedPage)
  }

  test("page from string") {
    val element = HtmlElement.pageFromString(sampleFragment)
    assertLinesSame(element.text, sampleGeneratedPage)
  }

  test("fragment from string") {
    val element = HtmlElement.fragmentFromString(sampleFragment)
    assert(element.text === sampleFragment)
  }

  test("remove") {
    val starting = HtmlElement.fragmentFromString(
      """<ul>
        |  <li>aaa</li>
        |  <li class="remove-me">bbb</li>
        |  <li>ccc</li>
        |</ul>
      """.stripMargin)
    val removed = starting.remove(".remove-me")
    val expected =
      """<ul>
        |  <li>aaa</li>
        |  <li>ccc</li>
        |</ul>
      """.stripMargin
    assertLinesSame(removed.text, expected)
  }

  test("select") {
    val starting = HtmlElement.fragmentFromString(
      """<ul>
        |  <li>aaa</li>
        |  <li class="select-me">bbb</li>
        |  <li>ccc</li>
        |</ul>
      """.stripMargin)
    val selected = starting.select(".select-me")
    val expected = """<li class="select-me">bbb</li>"""
    assertLinesSame(selected.text, expected)
  }

  test("append") {
    val starting = HtmlElement.fragmentFromString( """<div><ul class="append-to-me"></ul></div>""")
    val items = Seq("aaa", "bbb", "ccc")
    val children = items.map(x => HtmlElement.fragmentFromString(s"<li>$x</li>"))
    val appended = starting.append(".append-to-me", children)
    val expected =
      """<div>
        |<ul class="append-to-me">
        |  <li>aaa</li>
        |  <li>bbb</li>
        |  <li>ccc</li>
        |</ul>
        |</div>""".stripMargin
    assertLinesSame(appended.text, expected)
  }

  def assertLinesSame(left: String, right: String): Unit = {
    val compare = LinesDifference.compare(left, right)
    assert(compare.isSame, compare.detailLines.mkString("\n", "\n", "\n"))
  }
}
