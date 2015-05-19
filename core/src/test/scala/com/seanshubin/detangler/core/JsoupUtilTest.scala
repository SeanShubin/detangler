package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.scalatest.FunSuite

class JsoupUtilTest extends FunSuite {
  val shouldRemoveClass = true
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
    val element: Element = JsoupUtil.extractFragment(document, "foo", shouldRemoveClass)
    assert(document.select(".foo").size() === 0)
    assert(element.attributes().size() === 0)
  }

//  def debug(document: Document, element: Element): Unit = {
//    document.outputSettings().indentAmount(2)
//    println("DOCUMENT")
//    println(document)
//    println("ELEMENT")
//    println(element)
//    Thread.sleep(100)
//  }

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
    val element: Element = JsoupUtil.extractFragment(document, "aaa", shouldRemoveClass)
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
      JsoupUtil.extractFragment(document, "foo", shouldRemoveClass)
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
    JsoupUtil.setText(document, "foo", "baz", shouldRemoveClass)
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
    JsoupUtil.setAnchor(document, "foo", "baz", "baz-link", shouldRemoveClass)
    assert(document.select("a").size() === 1)
    val element = document.select("a").get(0)
    assert(element.text() === "baz")
    assert(element.attr("href") === "baz-link")
  }
}
