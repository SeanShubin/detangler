package com.seanshubin.detangler.report

import java.nio.charset.Charset
import java.nio.file.Path

import com.seanshubin.detangler.modle.{Cycle, Detangled, Module, Single}

class Reporter(detangled: Detangled,
               directory: Path,
               filesContract: FilesContract,
               charset: Charset,
               classLoader: ClassLoaderContract,
               pageTextGenerator: PageTextGenerator) extends Runnable {
  override def run(): Unit = {
    copyResource("style.css", directory.resolve("style.css"))
    generatePage(detangled.root())
  }

  private def copyResource(name: String, destination: Path): Unit = {
    val inputStream = classLoader.getResourceAsStream(name)
    val outputStream = filesContract.newOutputStream(destination)
    IoUtil.copyInputStreamToOutputStream(inputStream, outputStream)
  }

  private def generatePage(module: Module): Unit = {
    module match {
      case single: Single =>
        val children = detangled.children(module)
        if (children.nonEmpty) {
          val content = pageTextGenerator.generateFor(single)
          val fileName = fileNameFor(single)
          val file = directory.resolve(fileName)
          filesContract.write(file, content.getBytes(charset))
          children.foreach(generatePage)
        }
      case cycle: Cycle =>
    }
  }

  private def fileNameFor(single: Single): String = {
    val result = if (single.path.isEmpty) "index.html"
    else sanitizeFileName(single.path.mkString("--")) + ".html"
    result
  }

  private def sanitizeFileName(fileName: String): String = {
    val illegalFilenameCharacters = "/?<>\\:*|\""
    val replacementCharacter = '-'
    def replaceCharacterIfApplicable(ch: Char): Char = if (illegalFilenameCharacters.contains(ch)) replacementCharacter else ch
    fileName.map(replaceCharacterIfApplicable)
  }
}
