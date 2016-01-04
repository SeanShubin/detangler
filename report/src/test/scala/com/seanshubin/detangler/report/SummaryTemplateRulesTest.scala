package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class SummaryTemplateRulesTest extends FunSuite {
  test("generate summary") {
    val templateText =
      """<ul class="contents">
        |  <li class="content">
        |    <a class="name" href="index.html">root</a>
        |  </li>
        |</ul>
        |<ul class="entry-points">
        |  <li class="entry-point">
        |    <a class="name" href="link">name</a>
        |    <p class="depth"></p>
        |    <p class="breadth"></p>
        |    <p class="transitive"></p>
        |  </li>
        |</ul>
        |<ul class="cycles">
        |  <li class="cycle">
        |    <a class="name" href="link">name</a>
        |    <p class="depth"></p>
        |    <p class="breadth"></p>
        |    <p class="transitive"></p>
        |  </li>
        |</ul>
      """.stripMargin

  }
}
