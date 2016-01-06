package com.seanshubin.detangler.zip

import java.nio.charset.StandardCharsets

import com.seanshubin.detangler.collection.SeqDifference
import org.scalatest.FunSuite

class ZipContentsIteratorTest extends FunSuite {
  test("iterator") {
    val expected =
      """data.zip/file-a.txt
        |  Hello A!
        |data.zip/file-b.txt
        |  Hello B!
        |data.zip/zip-a.zip/dir-a/
        |data.zip/zip-a.zip/dir-a/file-c.txt
        |  Hello C!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/file-d.txt
        |  Hello D!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/file-e.txt
        |  Hello E!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-c.zip/dir-c/
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-c.zip/dir-c/file-f.txt
        |  Hello F!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-c.zip/dir-c/file-g.txt
        |  Hello G!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-d.zip/dir-d/
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-d.zip/dir-d/file-h.txt
        |  Hello H!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-e.zip/dir-e/
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-e.zip/dir-e/file-i.txt
        |  Hello I!
        |data.zip/zip-a.zip/dir-a/zip-b.zip/dir-b/zip-e.zip/dir-e/file-j.txt
        |  Hello J!
        |data.zip/zip-a.zip/dir-a/zip-f.zip/dir-f/
        |data.zip/zip-a.zip/dir-a/zip-f.zip/dir-f/file-k.txt
        |  Hello K!
        |data.zip/zip-g.zip/dir-g/""".stripMargin.split( """\r\n|\r|\n""")
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
    val name = "data.zip"
    val inputStream = getClass.getClassLoader.getResourceAsStream(name)
    val iterator = new ZipContentsIterator(inputStream, name, isZip, ZipContentsIterator.AcceptAll)
    val actual = iterator.flatMap(operateOnCursor).toIndexedSeq.map(scrubLine)
    val difference = SeqDifference.diff(expected, actual)
    assert(difference.isSame, difference.messageLines.mkString("\n"))
  }

  def scrubLine(line: String): String = line.replaceAll("\\\\", "/")
}
