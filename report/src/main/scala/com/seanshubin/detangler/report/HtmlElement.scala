package com.seanshubin.detangler.report

import java.io.InputStream
import java.nio.charset.Charset

import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class HtmlElement(originalElement: Element) {
  def text = originalElement.toString

  def remove(cssQuery: String): HtmlElement = {
    val element = clonedElement
    select(cssQuery, element).remove()
    new HtmlElement(element)
  }

  def select(cssQuery: String): HtmlElement = {
    new HtmlElement(select(cssQuery, originalElement))
  }

  def append(cssQuery: String, children: Seq[HtmlElement]): HtmlElement = {
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

  def clonedElement: Element = originalElement.clone()

  private def select(cssQuery: String, from: Element): Element = {
    val elements = from.select(cssQuery)
    val size = elements.size()
    if (size != 1) throw new RuntimeException(s"$from\nExpected exactly 1 matching '$cssQuery', got $size")
    elements.get(0)
  }
}

object HtmlElement {
  def pageFromInputStream(inputStream: InputStream, charset: Charset): HtmlElement = {
    val baseUri = ""
    val document = Jsoup.parse(inputStream, charset.name(), baseUri)
    document.outputSettings().indentAmount(2)
    new HtmlElement(document)
  }

  def pageFromString(text: String): HtmlElement = {
    val document = Jsoup.parse(text)
    document.outputSettings().indentAmount(2)
    new HtmlElement(document)
  }

  def fragmentFromString(text: String): HtmlElement = {
    val document = Jsoup.parse(text)
    document.outputSettings().indentAmount(2)
    val elements = document.body().children()
    val size = elements.size()
    if (size != 1) throw new RuntimeException(s"$text\nExpected exactly 1 element in body got $size")
    new HtmlElement(elements.get(0))
  }
}
