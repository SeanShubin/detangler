package com.seanshubin.detangler.report

import java.io.InputStream
import java.nio.charset.Charset

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

import scala.collection.JavaConversions


class HtmlElement(originalElement: Element) {

  override def toString: String = originalElement.toString

  def remove(cssQuery: String): HtmlElement = {
    val element = clonedElement
    select(cssQuery, element).remove()
    new HtmlElement(element)
  }

  def removeAll(cssQuery: String): HtmlElement = {
    val element = clonedElement
    val elements = selectAll(cssQuery, element)
    elements.foreach(_.remove())
    new HtmlElement(element)
  }

  def select(cssQuery: String): HtmlElement = {
    new HtmlElement(select(cssQuery, originalElement))
  }

  def append(cssQuery: String, children: Iterable[HtmlElement]): HtmlElement = {
    val element = clonedElement
    val attachmentPoint = select(cssQuery, element)
    for {
      child <- children
    } {
      attachmentPoint.appendChild(child.clonedElement)
    }
    new HtmlElement(element)
  }

  def append(cssQuery: String, child: HtmlElement): HtmlElement = {
    append(cssQuery, Seq(child))
  }

  def replace(cssQuery: String, content: HtmlElement): HtmlElement = {
    val element = clonedElement
    select(cssQuery, element).replaceWith(content.clonedElement)
    new HtmlElement(element)
  }

  def text(): String = {
    originalElement.text()
  }

  def text(cssQuery: String, newText: String): HtmlElement = {
    val element = clonedElement
    val target = select(cssQuery, element)
    target.text(newText)
    new HtmlElement(element)
  }

  def attr(name: String): String = {
    originalElement.attr(name)
  }

  def attr(cssQuery: String, name: String, value: String): HtmlElement = {
    val element = clonedElement
    val target = select(cssQuery, element)
    target.attr(name, value)
    new HtmlElement(element)
  }

  def anchor(cssQuery: String, href: String, text: String): HtmlElement = {
    val element = clonedElement
    val target = select(cssQuery, element)
    target.attr("href", href)
    target.text(text)
    new HtmlElement(element)
  }

  def clonedElement: Element = originalElement.clone()

  private def select(cssQuery: String, from: Element): Element = {
    val elements = from.select(cssQuery)
    val size = elements.size()
    if (size != 1) throw new RuntimeException(s"$from\nExpected exactly 1 matching '$cssQuery', got $size")
    elements.get(0)
  }

  private def selectAll(cssQuery: String, from: Element): Iterable[Element] = {
    val elements = from.select(cssQuery)
    JavaConversions.collectionAsScalaIterable(elements)
  }
}

object HtmlElement {
  def pageFromInputStream(inputStream: InputStream, charset: Charset): HtmlElement = {
    val baseUri = ""
    val document = Jsoup.parse(inputStream, charset.name(), baseUri)
    documentSettings(document)
    new HtmlElement(document)
  }

  def pageFromString(text: String): HtmlElement = {
    val document = Jsoup.parse(text)
    documentSettings(document)
    new HtmlElement(document)
  }

  def fragmentFromString(text: String): HtmlElement = {
    val document = Jsoup.parse(text)
    documentSettings(document)
    val elements = document.body().children()
    val size = elements.size()
    if (size != 1) throw new RuntimeException(s"$text\nExpected exactly 1 element in body got $size")
    new HtmlElement(elements.get(0))
  }

  private def documentSettings(document: Document): Unit = {
    document.outputSettings().indentAmount(2)
    document.outputSettings().outline(true)
  }
}
