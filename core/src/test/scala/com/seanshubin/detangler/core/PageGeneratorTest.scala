package com.seanshubin.detangler.core

import org.jsoup.Jsoup
import org.scalatest.FunSuite

class PageGeneratorTest extends FunSuite {
  test("top page") {
    val classLoader:ClassLoader = this.getClass.getClassLoader
    val classLoaderIntegration:ClassLoaderIntegration = new ClassLoaderIntegrationImpl(classLoader)
    val resourceLoader:ResourceLoader = new ResourceLoaderImpl(classLoaderIntegration)
    val pageGenerator:PageGenerator = new PageGeneratorImpl(SampleData.detangled, resourceLoader)
    val page = pageGenerator.pageForId(SampleData.idRoot)
    val document = Jsoup.parse(page)
    assert(document.select("#group_a").size() === 1)
    //document.querySelectorAll('#group_a thead tr th').item(0).textContent
    assert(document.select("#group_a thead tr th").get(0).text() === "name")
    assert(document.select("#group_a thead tr th").get(1).text() === "depth")
    assert(document.select("#group_a thead tr th").get(2).text() === "complexity")
    assert(document.select("#group_a thead tr th").get(3).text() === "composed of")
    //document.querySelectorAll('#group_a tbody tr td').item(0).textContent
    assert(document.select("#group_a tbody tr td").get(0).text() === "group/a")
    assert(document.select("#group_a tbody tr td").get(1).text() === "1")
    assert(document.select("#group_a tbody tr td").get(2).text() === "2")
    //document.querySelectorAll('#group_a tbody tr td').item(3).querySelector('a').text
    assert(document.select("#group_a tbody tr td").get(3).select("a").text() === "parts")
    //document.querySelectorAll('#group_a tbody tr td').item(3).querySelector('a').attributes.href.nodeValue
    assert(document.select("#group_a tbody tr td").get(3).select("a").attr("href") === "group_a.html")
  }
}
