package com.seanshubin.detangler.maven.plugin

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.{Mojo, Parameter}

@Mojo(name = "detangler")
class DetanglerMojo extends AbstractMojo {
  @Parameter(property = "greeting.target", defaultValue = "defaultValue")
  var greetingTarget: String = "greetingTarget"

  override def execute(): Unit = {
    getLog.info(s"Hello, $greetingTarget")
  }

}
