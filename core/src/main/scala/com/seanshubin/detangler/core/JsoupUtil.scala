package com.seanshubin.detangler.core

import org.jsoup.nodes.Element

object JsoupUtil {
  def extractFragment(template:Element, className:String):Element = {
    val element = exactlyOneElementWithClass(template, className)
    removeClass(element, className)
    element.remove()
    element
  }

  def setText(template:Element, className:String, text:String): Unit ={
    val element = exactlyOneElementWithClass(template, className)
    removeClass(element, className)
    element.text(text)
  }

  def setAnchor(template:Element, className:String, text:String, href:String): Unit ={
    val element = exactlyOneElementWithClass(template, className)
    removeClass(element, className)
    element.text(text)
    element.attr("href", href)
  }

  private def exactlyOneElementWithClass(template:Element, className:String):Element = {
    val selector = s".$className"
    val elements = template.select(selector)
    if(elements.size == 1) elements.get(0)
    else throw new RuntimeException(s"Expected exactly one element matching '$selector', got ${elements.size}\n$template")
  }

  private def removeClass(element:Element, className:String): Unit ={
    element.removeClass(className)
    if(element.classNames().size == 0) {
      element.removeAttr("class")
    }
  }
}
