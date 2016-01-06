package com.seanshubin.detangler.scanner

import java.nio.file.Path
import java.util.zip.ZipEntry

import com.seanshubin.detangler.contract.FilesContract
import com.seanshubin.detangler.zip.{ZipContents, ZipContentsIterator}

class ZipScannerImpl(files: FilesContract, isCompressed: String => Boolean, acceptName: String => Boolean) extends ZipScanner {
  override def loadBytes(path: Path): Iterable[Seq[Byte]] = {
    val inputStream = files.newInputStream(path)
    val iterator = new ZipContentsIterator(inputStream, path.toString, isCompressed, acceptEntry)
    iterator.filter(zipContentsRelevant).map(_.bytes.toSeq).toIndexedSeq
  }

  def zipContentsRelevant(zipContents: ZipContents): Boolean = {
    FileTypes.isClass(zipContents.zipEntry.getName)
  }

  def acceptEntry(path: Seq[String], entry: ZipEntry): Boolean = acceptName(entry.getName)
}
