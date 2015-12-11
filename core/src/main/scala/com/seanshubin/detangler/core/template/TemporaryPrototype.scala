package com.seanshubin.detangler.core.template

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.seanshubin.detangler.core.{HtmlFragment, SampleData}

object TemporaryPrototype extends App {
  val templatePath = Paths.get("core", "src", "main", "resources", "new-template.html")
  val inputStream = Files.newInputStream(templatePath)
  val charset = StandardCharsets.UTF_8
  val pageTemplate = HtmlFragment.fromInputStream(inputStream, charset)
  val rules = new PageTemplateRules(pageTemplate, SampleData.detangled, SampleData.idRoot)
  val generated = rules.generate()
  println(generated)
}
