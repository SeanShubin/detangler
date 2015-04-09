package com.seanshubin.detangler.core

import com.seanshubin.detangler.core.html.HtmlPage

trait PageGenerator {
  def generatePageText(page: HtmlPage): String
}
