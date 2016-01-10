package com.seanshubin.detangler.report

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.{Path, Paths}

import com.seanshubin.detangler.contract._
import com.seanshubin.detangler.graphviz.{GraphGenerator, GraphGeneratorImpl}
import com.seanshubin.detangler.model.Detangled

object Prototype extends App {
  runFromPath(Paths.get("generated", "cycles-false"), SampleData.detangled)
  runFromPath(Paths.get("generated", "cycles-true"), SampleDataWithCycles.detangled)

  def runFromPath(directory: Path, detangled: Detangled): Unit = {
    val filesContract: FilesContract = FilesDelegate
    val charset: Charset = StandardCharsets.UTF_8
    val classLoader: ClassLoaderContract = new ClassLoaderDelegate(getClass.getClassLoader)
    val standaloneSummaryTemplateRules: StandaloneSummaryTemplateRules = new StandaloneSummaryTemplateRulesImpl(detangled)
    val dependsOnTemplateRules: DependencyTemplateRules = new DependencyTemplateRulesImpl(detangled, DependencyDirection.TowardDependsOn)
    val dependedOnByTemplateRules: DependencyTemplateRules = new DependencyTemplateRulesImpl(detangled, DependencyDirection.TowardDependedOnBy)
    val standaloneTemplateRules: StandaloneTemplateRules = new StandaloneTemplateRulesImpl(standaloneSummaryTemplateRules, dependsOnTemplateRules, dependedOnByTemplateRules)
    val cycleTemplateRules: CycleTemplateRules = new CycleTemplateRulesImpl(detangled, dependsOnTemplateRules, dependedOnByTemplateRules)
    val modulesTemplateRules: ModulesTemplateRules = new ModulesTemplateRulesImpl(standaloneTemplateRules, cycleTemplateRules, detangled)
    val reasonsTemplateRules: ReasonsTemplateRules = new ReasonsTemplateRulesImpl(detangled)
    val pageTemplateRules: PageTemplateRules = new PageTemplateRulesImpl(
      modulesTemplateRules,
      reasonsTemplateRules)
    val graphTemplateRules: GraphTemplateRules = new GraphTemplateRulesImpl
    val graphGenerator: GraphGenerator = new GraphGeneratorImpl
    val createProcessBuilder: Seq[String] => ProcessBuilderContract = (command) => new ProcessBuilderDelegate(new ProcessBuilder(command: _*))
    val summaryTemplateRules: SummaryTemplateRules = new SummaryTemplateRulesImpl(detangled)
    val reporter: Runnable = new Reporter(
      detangled,
      directory,
      filesContract,
      charset,
      classLoader,
      summaryTemplateRules,
      pageTemplateRules,
      graphTemplateRules,
      graphGenerator,
      createProcessBuilder)
    reporter.run()
  }
}
