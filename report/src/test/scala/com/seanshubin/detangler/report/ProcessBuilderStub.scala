package com.seanshubin.detangler.report

import java.io.File

import com.seanshubin.detangler.contract.{ProcessBuilderContract, ProcessBuilderNotImplemented}

case class ProcessBuilderStub(theCommand: Seq[String], theProcess: Process, theDirectory: File = null) extends ProcessBuilderNotImplemented {
  override def directory(directory: File): ProcessBuilderContract = {
    copy(theDirectory = directory)
  }

  override def start(): Process = {
    theProcess
  }
}
