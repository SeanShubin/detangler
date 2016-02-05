package com.seanshubin.detangler.report

import com.seanshubin.detangler.model.{Cycle, Standalone}
import org.scalatest.FunSuite

class SummaryTemplateRulesTest extends FunSuite {
  test("generate summary") {
    val templateText =
      """<div>
        |<ul class="contents">
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
        |</div>
      """.stripMargin
    val template = HtmlElement.fragmentFromString(templateText)
    val classA = Standalone(Seq("a"))
    val classB = Standalone(Seq("b"))
    val classC = Standalone(Seq("c"))
    val classD = Standalone(Seq("d"))
    val classE = Standalone(Seq("e"))
    val classF = Standalone(Seq("f"))
    val cycleCD = Cycle(Set(classC, classD))
    val cycleEF = Cycle(Set(classE, classF))
    val entryPoints = Seq(classA, classB)
    val cycles = Seq(cycleCD, cycleEF)
  }
}
