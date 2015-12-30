package com.seanshubin.detangler.zip

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.seanshubin.detangler.collection.SeqDifference
import org.scalatest.FunSuite

class ZipContentsIteratorTest extends FunSuite {
  test("iterator") {
    val expected =
      """zip/sample-data/data.zip/file-a.txt
        |  Hello A!
        |zip/sample-data/data.zip/file-b.txt
        |  Hello B!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/
        |zip/sample-data/data.zip/zip-a.zip/dir-a/file-c.txt
        |  Hello C!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/file-d.txt
        |  Hello D!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/file-e.txt
        |  Hello E!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-c.zip/dir-c/
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-c.zip/dir-c/file-f.txt
        |  Hello F!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-c.zip/dir-c/file-g.txt
        |  Hello G!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-d.zip/dir-d/
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-d.zip/dir-d/file-h.txt
        |  Hello H!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-e.zip/dir-e/
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-e.zip/dir-e/file-i.txt
        |  Hello I!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-e.zip/dir-e/file-j.txt
        |  Hello J!
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-f.zip/dir-f/
        |zip/sample-data/data.zip/zip-a.zip/dir-a/zip-f.zip/dir-f/file-k.txt
        |  Hello K!
        |zip/sample-data/data.zip/zip-g.zip/dir-g/""".stripMargin.split( """\r\n|\r|\n""")
    def isZip(name: String) = name.endsWith(".zip")
    def operateOnCursor(cursor: ZipContents): Seq[String] = {
      val ZipContents(path, zipEntry, bytes) = cursor
      val pathString = (path :+ zipEntry.getName).mkString("/")
      if (zipEntry.isDirectory) {
        Seq(pathString)
      } else {
        val content = new String(bytes, StandardCharsets.UTF_8)
        Seq(pathString, "  " + content)
      }
    }
    val file = Paths.get("zip", "sample-data", "data.zip")
    val inputStream = Files.newInputStream(file)
    val iterator = new ZipContentsIterator(inputStream, file.toString, isZip)
    val actual = iterator.flatMap(operateOnCursor).toIndexedSeq.map(scrubLine)
    val difference = SeqDifference.diff(expected, actual)
    assert(difference.isSame, difference.messageLines.mkString("\n"))
  }

  def scrubLine(line: String): String = line.replaceAll("\\\\", "/")
}
