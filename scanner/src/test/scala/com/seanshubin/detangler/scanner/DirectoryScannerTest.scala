package com.seanshubin.detangler.scanner

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitor, Path, Paths}

import com.seanshubin.detangler.contract.FilesContract
import com.seanshubin.detangler.contract.test.FilesNotImplemented
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class DirectoryScannerTest extends FunSuite {
  test("the correct files") {
    val searchPaths = Seq("foo", "bar")
    val files = Seq("find-this.jar", "find-this.class", "do-not-find-this.txt", "find-this.war")
    val filesContract: FilesContract = new FilesContractStub(files)
    val ignoreFiles: Seq[Path] = Seq()
    val fileScanner = new DirectoryScannerImpl(filesContract, searchPaths.map(stringToPath), ignoreFiles)
    val actual = fileScanner.findFiles()
    val expected = Seq(
      Paths.get("foo", "find-this.jar"),
      Paths.get("foo", "find-this.class"),
      Paths.get("foo", "find-this.war"),
      Paths.get("bar", "find-this.jar"),
      Paths.get("bar", "find-this.class"),
      Paths.get("bar", "find-this.war")
    )
    assert(actual === expected)
  }

  private def stringToPath(pathName: String): Path = Paths.get(pathName)

  class FilesContractStub(fileNamesToFind: Seq[String]) extends FilesNotImplemented {
    val pathsWalked = new ArrayBuffer[Path]

    override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Path = {
      val dummyAttributes: BasicFileAttributes = null
      fileNamesToFind.map(stringToPath).map(file => visitor.visitFile(start.resolve(file), dummyAttributes))
      start
    }
  }

}
