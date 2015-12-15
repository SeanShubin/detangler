package com.seanshubin.detangler.report

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Path, Paths}

import com.seanshubin.detangler.model.Detangled

object Prototype extends App {
  val detangled: Detangled = SampleData.detangled
  val directory: Path = Paths.get("generated")
  val filesContract: FilesContract = FilesDelegate
  val charset: Charset = StandardCharsets.UTF_8
  val classLoader: ClassLoaderContract = new ClassLoaderDelegate(getClass.getClassLoader)
  val singleSummaryTemplateRules: SingleSummaryTemplateRules = new SingleSummaryTemplateRulesImpl(detangled)
  val singleDetailTemplateRules: DependencyTemplateRules = new DependencyTemplateRulesImpl()
  val singleTemplateRules: SingleTemplateRules = new SingleTemplateRulesImpl(singleSummaryTemplateRules, singleDetailTemplateRules)
  val cycleTemplateRules: CycleTemplateRules = new CycleTemplateRulesImpl()
  val modulesTemplateRules: ModulesTemplateRules = new ModulesTemplateRulesImpl(singleTemplateRules, cycleTemplateRules, detangled)
  val reasonsTemplateRules: ReasonsTemplateRules = new ReasonsTemplateRulesImpl()
  val pageTemplateRules: PageTemplateRules = new PageTemplateRulesImpl(modulesTemplateRules, reasonsTemplateRules)
  val reporter: Runnable = new Reporter(detangled, directory, filesContract, charset, classLoader, pageTemplateRules)
  reporter.run()
}
