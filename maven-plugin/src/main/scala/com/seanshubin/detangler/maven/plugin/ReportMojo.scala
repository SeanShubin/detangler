package com.seanshubin.detangler.maven.plugin

import com.seanshubin.detangler.console.ConsoleApplication
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.{Mojo, Parameter}

@Mojo(name = "report")
class ReportMojo extends AbstractMojo {
  @Parameter(defaultValue = "${detanglerConfig}")
  var detanglerConfig: String = null

  override def execute(): Unit = {
    ConsoleApplication.main(Array(detanglerConfig))
  }
}
