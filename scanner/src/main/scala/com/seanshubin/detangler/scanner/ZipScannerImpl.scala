package com.seanshubin.detangler.scanner

import java.nio.file.Path

import com.seanshubin.detangler.contract.FilesContract
import com.seanshubin.detangler.zip.{ZipContents, ZipContentsIterator}

class ZipScannerImpl(files: FilesContract, isCompressed: String => Boolean) extends ZipScanner {
  override def loadBytes(path: Path): Iterable[Seq[Byte]] = {
    val inputStream = files.newInputStream(path)
    val iterator = new ZipContentsIterator(inputStream, path.toString, isCompressed)
    iterator.filter(zipContentsRelevant).map(display).map(_.bytes.toSeq).toIndexedSeq
  }

  def zipContentsRelevant(zipContents: ZipContents): Boolean = {
    FileTypes.isClass(zipContents.path.last)
  }

  def display(zipContents: ZipContents): ZipContents = {
    val pathString = zipContents.path.mkString("/")
    val bytesString = s"${zipContents.bytes.length} bytes"
    println(s"$pathString $bytesString")
    zipContents
  }
}
