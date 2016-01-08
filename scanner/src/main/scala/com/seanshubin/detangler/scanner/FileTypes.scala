package com.seanshubin.detangler.scanner

object FileTypes {
  private val classExt = ".class"
  private val jarExt = ".jar"
  private val warExt = ".war"
  private val relevantExtensions = Seq(classExt, jarExt, warExt)
  private val compressedExtensions = Seq(jarExt, warExt)

  def isClass(path: String): Boolean = {
    path.endsWith(classExt)
  }

  def isRelevant(path: String): Boolean = {
    relevantExtensions.exists(path.endsWith)
  }

  def isCompressed(path: String): Boolean = {
    compressedExtensions.exists(path.endsWith)
  }

  def isJar(path: String): Boolean = {
    path.endsWith(jarExt)
  }
}
