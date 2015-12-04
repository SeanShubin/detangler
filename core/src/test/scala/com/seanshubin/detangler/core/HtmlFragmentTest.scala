package com.seanshubin.detangler.core

import org.scalatest.FunSuite

class HtmlFragmentTest extends FunSuite {
  test("convert back and forth using jsoup") {
    val text = "<p>hello</p>"
    val original = HtmlFragment.fromText(text)
    assert(original.text === text)
  }

  test("build list of links from template") {
    val template = HtmlFragment.fromText("<ul class=\"outer\"><li class=\"inner\"><a class=\"anchor\" href=\"link\">text</a></li></ul>")
    val (list, item) = template.splitOneIntoOuterAndInner("li")
    val itemA = item.updateAnchor(".anchor", "linkA", "textA")
    val itemB = item.updateAnchor(".anchor", "linkB", "textB")
    val itemC = item.updateAnchor(".anchor", "linkC", "textC")
    val composed = list.appendChild(itemA).appendChild(itemB).appendChild(itemC)
    val expected =
      """<ul class="outer">
        | <li class="inner"><a class="anchor" href="linkA">textA</a></li>
        | <li class="inner"><a class="anchor" href="linkB">textB</a></li>
        | <li class="inner"><a class="anchor" href="linkC">textC</a></li>
        |</ul>""".stripMargin
    val actual = composed.text
    val difference = LinesDifference.compare(actual, expected)
    assert(difference.isSame, difference.detailLines)
  }
}
