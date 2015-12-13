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

  def append(cssQuery: String, children: HtmlElement*): HtmlElement = {
    val element = clonedElement
    val attachmentPoint = select(cssQuery, element)
    for {
      child <- children
    } {
      attachmentPoint.appendChild(child.clonedElement)
    }
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
  def fromInputStream(inputStream: InputStream, charset: Charset): HtmlElement = {
    val baseUri = ""
    val document = Jsoup.parse(inputStream, charset.name(), baseUri)
    document.outputSettings().indentAmount(2)
    new HtmlElement(document)
  }
}