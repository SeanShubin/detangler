package com.seanshubin.detangler.report

sealed trait GraphRenderResult

case object GraphRenderSuccess extends GraphRenderResult

case class GraphRenderFailure(command: Seq[String]) extends GraphRenderResult
