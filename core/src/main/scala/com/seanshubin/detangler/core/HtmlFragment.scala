package com.seanshubin.detangler.core

import java.io.InputStream
import java.nio.charset.Charset

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
Jsoup is designed to deal with full documents as mutable entities, which makes composition difficult
This class hides the complexity incurred by mutability, and makes it easier to deal with fragments rather than full documents
 */
sealed trait HtmlFragment {
  def text: String

  def clonedElement: Element

  def one(cssQuery: String): HtmlFragment

  def first(cssQuery: String): HtmlFragment

  def all(cssQuery: String): Seq[HtmlFragment]

  def splitOneIntoOuterAndInner(cssQuery: String): (HtmlFragment, HtmlFragment)

  def appendChild(that: HtmlFragment): HtmlFragment

  def appendAll(cssQuery: String, fragments: Seq[HtmlFragment]): HtmlFragment

  def attr(cssQuery: String, name: String, value: String): HtmlFragment

  def attr(cssQuery: String, name: String): String

  def firstAttr(cssQuery: String, name: String): String

  def text(cssQuery: String, text: String): HtmlFragment

  def text(cssQuery: String): String

  def firstText(cssQuery: String): String

  def remove(cssQuery: String): HtmlFragment

  def removeContents(): HtmlFragment

  def anchor(cssQuery: String, href: String, text: String): HtmlFragment

  def becomeChildOf(that: HtmlFragment): HtmlFragment
}

class SingleElementHtmlFragment(originalElement: Element) extends HtmlFragment {

  import HtmlFragment._

  override def text: String = originalElement.toString

  override def clonedElement: Element = originalElement.clone()

  override def toString: String = text

  override def one(cssQuery: String): HtmlFragment = {
    val element = findExactlyOne(cssQuery, originalElement)
    new SingleElementHtmlFragment(element)
  }

  override def first(cssQuery: String): HtmlFragment = {
    val element = findFirst(cssQuery, originalElement)
    new SingleElementHtmlFragment(element)
  }

  override def all(cssQuery: String): Seq[HtmlFragment] = {
    val matching = originalElement.select(cssQuery)
    val elements = for {
      index <- 0 until matching.size()
    } yield {
        matching.get(index)
      }
    val fragments = elements.map(new SingleElementHtmlFragment(_))
    fragments
  }

  override def splitOneIntoOuterAndInner(cssQuery: String): (HtmlFragment, HtmlFragment) = {
    val outer = originalElement.clone()
    val inner = findExactlyOne(cssQuery, outer)
    inner.remove()
    (new SingleElementHtmlFragment(outer), HtmlFragment.fromText(inner.outerHtml()))
  }

  override def appendChild(that: HtmlFragment): HtmlFragment = {
    that.becomeChildOf(this)
  }

  override def becomeChildOf(that: HtmlFragment): HtmlFragment = {
    val child: Element = that.clonedElement.appendChild(this.clonedElement)
    new SingleElementHtmlFragment(child)

  }

  override def appendAll(cssQuery: String, fragments: Seq[HtmlFragment]): HtmlFragment = {
    val element = originalElement.clone()
    val attachTo = findExactlyOne(cssQuery, element)
    for {
      fragment <- fragments
    } {
      attachTo.appendChild(fragment.clonedElement)
    }
    new SingleElementHtmlFragment(element)
  }

  override def attr(cssQuery: String, name: String, value: String): HtmlFragment = {
    val element = originalElement.clone()
    val toModify = findExactlyOne(cssQuery, element)
    toModify.attr(name, value)
    new SingleElementHtmlFragment(element)
  }

  override def attr(cssQuery: String, name: String): String = {
    findExactlyOne(cssQuery, originalElement).attr(name)
  }

  override def firstAttr(cssQuery: String, name: String): String = {
    findFirst(cssQuery, originalElement).attr(name)
  }

  override def text(cssQuery: String, text: String): HtmlFragment = {
    val element = originalElement.clone()
    val toModify = findExactlyOne(cssQuery, element)
    toModify.text(text)
    new SingleElementHtmlFragment(element)
  }

  override def text(cssQuery: String): String = {
    findExactlyOne(cssQuery, originalElement).text()
  }

  override def firstText(cssQuery: String): String = {
    findFirst(cssQuery, originalElement).text()
  }

  override def remove(cssQuery: String): HtmlFragment = {
    val element = originalElement.clone()
    findExactlyOne(cssQuery, element).remove()
    new SingleElementHtmlFragment(element)
  }

  override def removeContents(): HtmlFragment = {
    val clone: Element = originalElement.clone()
    clone.children().remove()
    new SingleElementHtmlFragment(clone)
  }

  override def anchor(cssQuery: String, href: String, text: String): HtmlFragment = {
    val element = originalElement.clone()
    val toModify = findExactlyOne(cssQuery, element)
    toModify.attr("href", href)
    toModify.text(text)
    new SingleElementHtmlFragment(element)
  }
}

class EmptyHtmlFragment extends HtmlFragment {
  override def text: String = ???

  override def text(cssQuery: String, text: String): HtmlFragment = ???

  override def text(cssQuery: String): String = ???

  override def splitOneIntoOuterAndInner(cssQuery: String): (HtmlFragment, HtmlFragment) = ???

  override def one(cssQuery: String): HtmlFragment = ???

  override def firstAttr(cssQuery: String, name: String): String = ???

  override def appendAll(cssQuery: String, fragments: Seq[HtmlFragment]): HtmlFragment = ???

  override def firstText(cssQuery: String): String = ???

  override def clonedElement: Element = ???

  override def all(cssQuery: String): Seq[HtmlFragment] = ???

  override def anchor(cssQuery: String, href: String, text: String): HtmlFragment = ???

  override def remove(cssQuery: String): HtmlFragment = ???

  override def removeContents(): HtmlFragment = ???

  override def appendChild(that: HtmlFragment): HtmlFragment = ???

  override def attr(cssQuery: String, name: String, value: String): HtmlFragment = ???

  override def attr(cssQuery: String, name: String): String = ???

  override def first(cssQuery: String): HtmlFragment = ???

  override def becomeChildOf(that: HtmlFragment): HtmlFragment = that
}

object HtmlFragment {
  val Empty = new EmptyHtmlFragment

  def fromText(text: String): HtmlFragment = {
    val document = Jsoup.parse(text)
    val body = document.body()
    val children = body.children()
    if (children.size() != 1)
      throw new RuntimeException(s"Expected only a single element in:\n$text\ngot ${children.size()}")
    val element = children.get(0)
    val fragment = new SingleElementHtmlFragment(element)
    fragment
  }

  def fromInputStream(inputStream: InputStream, charset: Charset): HtmlFragment = {
    val text = IoUtil.inputStreamToString(inputStream, charset)
    fromText(text)
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
