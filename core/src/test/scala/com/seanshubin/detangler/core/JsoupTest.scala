package com.seanshubin.detangler.core

import java.nio.charset.StandardCharsets

import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.scalatest.FunSuite

import scala.collection.JavaConversions

class JsoupTest extends FunSuite {
  test("compose") {
    val table = loadResource("table.html")
    val page = loadResource("empty-page.html")
//    println(table.outerHtml())
//    println(page.outerHtml())
    println(table.body().select("table").size())
    val element:Element = table.body().select("table").get(0)
    println(element.childNodes().size())
//    page.body().appendChild(element.)
//    for {
//      node <- JavaConversions.iterableAsScalaIterable(table.body().childNodes())
//    } {
//      println("=" * 10)
//      println(node)
//    }
//    page.body().append()
  }

  def loadResource(name:String):Document = {
    val in = getClass.getClassLoader.getResourceAsStream(name)
    val charset = StandardCharsets.UTF_8
    val charsetName = charset.name()
    val doc:Document = Jsoup.parse(in, charsetName, "")
    doc
  }
}
