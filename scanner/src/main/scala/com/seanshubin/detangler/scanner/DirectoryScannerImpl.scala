package com.seanshubin.detangler.scanner

import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path}

import com.seanshubin.detangler.contract.FilesContract

import scala.collection.mutable.ArrayBuffer

class DirectoryScannerImpl(files: FilesContract, searchPaths: Seq[Path]) extends DirectoryScanner {
  override def findFiles(): Iterable[Path] = {
    searchPaths.flatMap(findFilesFromPath)
  }

  private def findFilesFromPath(path: Path): Iterable[Path] = {
    val relevantFileCollector = new RelevantFileCollector
    files.walkFileTree(path, relevantFileCollector)
    relevantFileCollector.filesFound
  }

  private class RelevantFileCollector extends FileVisitor[Path] {
    val filesFound = new ArrayBuffer[Path]

    override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = {
      throw new RuntimeException("visitFileFailed: " + file)
    }

    override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
      if (FileTypes.isRelevant(file)) {
        filesFound.append(file)
      }
      FileVisitResult.CONTINUE
    }

    override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
      FileVisitResult.CONTINUE
    }

    override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
      FileVisitResult.CONTINUE
    }
  }

}
