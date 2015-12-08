package com.seanshubin.detangler.core

trait PageGenerator {
  def pageForId(id: UnitId, template: HtmlFragment): String
}
