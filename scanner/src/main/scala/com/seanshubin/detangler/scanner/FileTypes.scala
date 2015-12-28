package com.seanshubin.detangler.scanner

import java.nio.file.Path

object FileTypes {
  private val classExt = ".class"
  private val jarExt = ".jar"
  private val warExt = ".war"
  private val relevantExtensions = Seq(classExt, jarExt, warExt)
  private val compressedExtensions = Seq(jarExt, warExt)

  def isRelevant(path: Path): Boolean = {
    relevantExtensions.exists(path.toString.endsWith)
  }

  def isCompressed(path: Path): Boolean = {
    compressedExtensions.exists(path.toString.endsWith)
  }
}
