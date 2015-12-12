package com.seanshubin.detangler.report

import com.seanshubin.detangler.modle.Single

trait PageTextGenerator {
  def generateFor(single: Single): String
}
