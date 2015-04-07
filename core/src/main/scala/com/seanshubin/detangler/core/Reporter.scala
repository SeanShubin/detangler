package com.seanshubin.detangler.core

trait Reporter {
  def generateReportsOne(detangled: Detangled)
  def generateReportsTwo(detangled: Detangled)
}
