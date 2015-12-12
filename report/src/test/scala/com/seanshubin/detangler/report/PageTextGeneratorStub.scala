package com.seanshubin.detangler.report

import com.seanshubin.detangler.modle.Single

class PageTextGeneratorStub(contentMap: Map[Single, String]) extends PageTextGenerator {
  override def generateFor(single: Single): String = contentMap(single)
}
