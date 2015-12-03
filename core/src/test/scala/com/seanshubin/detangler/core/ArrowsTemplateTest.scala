package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class ArrowsTemplateTest extends FunSuite {
  test("arrows template") {
    val templateText =
      """<ul class="reasons">
        |    <li class="reason">
        |        <a class="from" href="replace-me">replace-me</a>
        |        <span>&rarr;</span>
        |        <a class="to" href="replace-me">replace-me</a>
        |    </li>
        |</ul>
        | """.stripMargin
    val arrows =
      Seq(Arrow(SampleData.idGroupA, SampleData.idGroupB,
        Seq(Arrow(SampleData.idPackageC, SampleData.idPackageE,
          Seq(Arrow(SampleData.idClassF, SampleData.idClassI, Seq()))))))
    val replacedText = TemplateUtil.arrows(templateText, arrows)
    println(replacedText)
  }
}
