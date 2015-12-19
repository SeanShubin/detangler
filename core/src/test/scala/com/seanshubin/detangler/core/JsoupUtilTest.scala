package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.scalatest.FunSuite

class JsoupUtilTest extends FunSuite {
  val shouldRemoveClass = true
  val jsoupUtil = new JsoupUtil(shouldRemoveClass)
  test("extract fragment") {
    val sample =
      """<!DOCTYPE html>
        |<html>
        |<head lang="en">
        |    <meta charset="UTF-8">
        |    <title>dependency report</title>
        |    <link rel="stylesheet" type="text/css" href="style.css">
        |</head>
        |<body>
        |  <div class="foo">bar</div>
        |</body>
        |</html>
        | """.stripMargin
    val document: Document = Jsoup.parse(sample)
    val element: Element = jsoupUtil.extractFragment(document, "foo")
    assert(document.select(".foo").size() === 0)
    assert(element.attributes().size() === 0)
  }

  test("extract fragment with multiple classes") {
    val sample =
      """<!DOCTYPE html>
        |<html>
        |<head lang="en">
        |    <meta charset="UTF-8">
        |    <title>dependency report</title>
        |    <link rel="stylesheet" type="text/css" href="style.css">
        |</head>
        |<body>
        |  <div class="aaa bbb">bar</div>
        |</body>
        |</html>
        | """.stripMargin
    val document: Document = Jsoup.parse(sample)
    val element: Element = jsoupUtil.extractFragment(document, "aaa")
    assert(document.select(".foo").size() === 0)
    assert(element.attributes().size() === 1)
    assert(element.classNames().size() === 1)
    assert(element.classNames().contains("bbb"))
  }

  test("extract fragment throws exception on multiple ids") {
    val sample =
      """<!DOCTYPE html>
        |<html>
        |<head lang="en">
        |    <meta charset="UTF-8">
        |    <title>dependency report</title>
        |    <link rel="stylesheet" type="text/css" href="style.css">
        |</head>
        |<body>
        |  <div class="foo">bar</div>
        |  <div class="foo">bar</div>
        |</body>
        |</html>
        | """.stripMargin
    val document = Jsoup.parse(sample)
    val exception = intercept[RuntimeException] {
      jsoupUtil.extractFragment(document, "foo")
    }
    assert(exception.getMessage.contains("Expected exactly one element matching '.foo', got 2"))
  }

  test("set text") {
    val sample =
      """<!DOCTYPE html>
        |<html>
        |<head lang="en">
        |    <meta charset="UTF-8">
        |    <title>dependency report</title>
        |    <link rel="stylesheet" type="text/css" href="style.css">
        |</head>
        |<body>
        |  <div class="foo">bar</div>
        |</body>
        |</html>
        | """.stripMargin
    val document: Document = Jsoup.parse(sample)
    jsoupUtil.setText(document, "foo", "baz")
    assert(document.select("div").size() === 1)
    val element = document.select("div").get(0)
    assert(element.text() === "baz")
    assert(element.attributes().size() === 0)
  }

  test("set anchor") {
    val sample =
      """<!DOCTYPE html>
        |<html>
        |<head lang="en">
        |    <meta charset="UTF-8">
        |    <title>dependency report</title>
        |    <link rel="stylesheet" type="text/css" href="style.css">
        |</head>
        |<body>
        |  <a class="foo">bar</a>
        |</body>
        |</html>
        | """.stripMargin
    val document: Document = Jsoup.parse(sample)
    jsoupUtil.setAnchor(document, "foo", "baz", "baz-link")
    assert(document.select("a").size() === 1)
    val element = document.select("a").get(0)
    assert(element.text() === "baz")
    assert(element.attr("href") === "baz-link")
  }
}
