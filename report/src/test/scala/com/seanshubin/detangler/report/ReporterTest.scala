package com.seanshubin.detangler.report

import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  test("generate report") {
    //given
    val sideEffects = new SideEffects
    val reporter: Runnable = new Reporter(SampleData.detangled)

    //when
    reporter.run()

    //then
    assert(sideEffects.filesGenerated === Set(
      "group-a--package-c.html",
      "group-a--package-d.html",
      "group-a.html",
      "group-b--package-e.html",
      "group-b.html",
      "index.html",
      "style.css"
    ))
  }

}
