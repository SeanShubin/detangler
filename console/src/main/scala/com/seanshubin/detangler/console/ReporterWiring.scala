package com.seanshubin.detangler.console

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.Path

import com.seanshubin.detangler.contract._
import com.seanshubin.detangler.core.{ConfigurationWriter, Notifications}
import com.seanshubin.detangler.graphviz.{GraphGenerator, GraphGeneratorImpl}
import com.seanshubin.detangler.model.{Detangled, Standalone}
import com.seanshubin.detangler.report._

trait ReporterWiring {
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
  val graphTemplateRules: GraphTemplateRules = new GraphTemplateRulesImpl()
  val graphGenerator: GraphGenerator = new GraphGeneratorImpl
  val createProcessBuilder: Seq[String] => ProcessBuilderContract = (commands) => new ProcessBuilderDelegate(new ProcessBuilder(commands: _*))
  val summaryTemplateRules: SummaryTemplateRules = new SummaryTemplateRulesImpl(detangled, allowedCycles)
  val reporter: Runnable = new Reporter(
    detangled,
    allowedCycles,
    reportDir,
    filesContract,
    charset,
    classLoader,
    summaryTemplateRules,
    pageTemplateRules,
    graphTemplateRules,
    graphGenerator,
    createProcessBuilder,
    configurationWriter.configurationLines,
    configurationWriter.configurationLinesAllowCycles,
    notifications.newCycleParts)

  def detangled: Detangled

  def reportDir: Path

  def allowedCycles: Seq[Standalone]

  def configurationWriter: ConfigurationWriter

  def notifications: Notifications
}
