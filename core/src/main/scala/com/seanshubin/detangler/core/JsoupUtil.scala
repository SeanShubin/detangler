package com.seanshubin.detangler.core

import org.jsoup.nodes.Element

class JsoupUtil(shouldRemoveClass:Boolean) {
  def extractFragment(template: Element, className: String): Element = {
    val element = exactlyOneElementWithClass(template, className)
    removeClassIf(element, className)
    element.remove()
    element
  }

  def setText(template: Element, className: String, text: String): Unit = {
    val element = exactlyOneElementWithClass(template, className)
    removeClassIf(element, className)
    element.text(text)
  }

  def setAnchor(template: Element, className: String, text: String, href: String): Unit = {
    val element = exactlyOneElementWithClass(template, className)
    removeClassIf(element, className)
    element.text(text)
    element.attr("href", href)
  }

  private def exactlyOneElementWithClass(template: Element, className: String): Element = {
    val selector = s".$className"
    JsoupUtil.exactlyOneElement(template, selector)
  }

  private def removeClassIf(element: Element, className: String): Unit = {
    if (!shouldRemoveClass) return
    element.removeClass(className)
    if (element.classNames().size == 0) {
      element.removeAttr("class")
    }
  }
}

object JsoupUtil {
  def exactlyOneElement(template: Element, selector: String): Element = {
    val elements = template.select(selector)
    if (elements.size == 1) elements.get(0)
    else throw new RuntimeException(s"Expected exactly one element matching '$selector', got ${elements.size}\n$template")
  }
  def firstElement(template: Element, selector: String): Element = {
    val elements = template.select(selector)
    if (elements.size > 0) elements.get(0)
    else throw new RuntimeException(s"Expected at least one element matching '$selector', got ${elements.size}\n$template")
  }
}