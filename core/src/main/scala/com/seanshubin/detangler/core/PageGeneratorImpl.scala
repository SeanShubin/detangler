package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html.HtmlPage

class PageGeneratorImpl extends PageGenerator {
  override def generatePage(page: HtmlPage): Unit = {
    println(page.fileName)
  }
}
