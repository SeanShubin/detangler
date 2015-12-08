package com.seanshubin.detangler.core

trait PageGenerator {
  def pageForId(id: UnitId): String
}
