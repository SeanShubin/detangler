package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.Standalone

trait PageTemplateRules {
  def generate(pageTemplate: HtmlElement, standalone: Standalone, isLeafPage: Boolean): HtmlElement
}
