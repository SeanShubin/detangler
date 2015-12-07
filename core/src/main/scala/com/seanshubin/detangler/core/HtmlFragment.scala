package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
Jsoup is designed to deal with full documents as mutable entities, which makes composition difficult
This class hides the complexity incurred by mutability, and makes it easier to deal with fragments rather than full documents
 */
class HtmlFragment(originalElement: Element) {

  import HtmlFragment._

  def text: String = originalElement.toString

  def clonedElement: Element = originalElement.clone()

  override def toString: String = text

  def one(cssQuery: String): HtmlFragment = {
    val element = findExactlyOne(cssQuery, originalElement)
    new HtmlFragment(element)
  }

  def first(cssQuery: String): HtmlFragment = {
    val element = findFirst(cssQuery, originalElement)
    new HtmlFragment(element)
  }

  def all(cssQuery: String): Seq[HtmlFragment] = {
    val matching = originalElement.select(cssQuery)
    val elements = for {
      index <- 0 until matching.size()
    } yield {
        matching.get(index)
      }
    val fragments = elements.map(new HtmlFragment(_))
    fragments
  }

  def splitOneIntoOuterAndInner(cssQuery: String): (HtmlFragment, HtmlFragment) = {
    val outer = originalElement.clone()
    val inner = findExactlyOne(cssQuery, outer)
    inner.remove()
    (new HtmlFragment(outer), HtmlFragment.fromText(inner.outerHtml()))
  }

  def appendChild(that: HtmlFragment): HtmlFragment = {
    val child: Element = originalElement.clone().appendChild(that.clonedElement)
    new HtmlFragment(child)
  }

  def appendAll(cssQuery: String, fragments: Seq[HtmlFragment]): HtmlFragment = {
    val element = originalElement.clone()
    val attachTo = findExactlyOne(cssQuery, element)
    for {
      fragment <- fragments
    } {
      attachTo.appendChild(fragment.clonedElement)
    }
    new HtmlFragment(element)
  }

  def attr(cssQuery: String, name: String, value: String): HtmlFragment = {
    val element = originalElement.clone()
    val toModify = findExactlyOne(cssQuery, element)
    toModify.attr(name, value)
    new HtmlFragment(element)
  }

  def attr(cssQuery: String, name: String): String = {
    findExactlyOne(cssQuery, originalElement).attr(name)
  }

  def text(cssQuery: String, text: String): HtmlFragment = {
    val element = originalElement.clone()
    val toModify = findExactlyOne(cssQuery, element)
    toModify.text(text)
    new HtmlFragment(element)
  }

  def text(cssQuery: String): String = {
    findExactlyOne(cssQuery, originalElement).text()
  }

  def delete(cssQuery: String): HtmlFragment = {
    val element = originalElement.clone()
    findExactlyOne(cssQuery, element).remove()
    new HtmlFragment(element)
  }

  def anchor(cssQuery: String, href: String, text: String): HtmlFragment = {
    val element = originalElement.clone()
    val toModify = findExactlyOne(cssQuery, element)
    toModify.attr("href", href)
    toModify.text(text)
    new HtmlFragment(element)

  }
}

object HtmlFragment {
  def fromText(text: String): HtmlFragment = {
    val document = Jsoup.parse(text)
    val body = document.body()
    val children = body.children()
    if (children.size() != 1)
      throw new RuntimeException(s"Expected only a single element in:\n$text\ngot ${children.size()}")
    val element = children.get(0)
    val fragment = new HtmlFragment(element)
    fragment
  }

  def findExactlyOne(cssQuery: String, element: Element): Element = {
    val matching = element.select(cssQuery)
    if (matching.size() == 1) matching.get(0)
    else throw new RuntimeException(s"In fragment:\n$element\nexpected exactly one element matching '$cssQuery', got ${matching.size()}")
  }

  def findFirst(cssQuery: String, element: Element): Element = {
    val matching = element.select(cssQuery)
    if (matching.size() == 0)
      throw new RuntimeException(s"In fragment:\n$element\nexpected at least one element matching '$cssQuery'")
    else matching.get(0)
  }
}
