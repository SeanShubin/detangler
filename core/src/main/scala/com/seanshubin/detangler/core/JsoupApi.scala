package com.seanshubin.detangler.core

trait JsoupApi {
  def parse(htmls: String): JsoupFragment

  def concat(left: JsoupFragment, right: JsoupFragment): JsoupFragment
}
