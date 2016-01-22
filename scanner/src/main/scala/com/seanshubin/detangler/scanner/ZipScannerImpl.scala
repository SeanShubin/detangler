package com.seanshubin.detangler.scanner

import java.io.InputStream
import java.nio.file.Path
import java.util.zip.ZipEntry

import com.seanshubin.detangler.contract.FilesContract
import com.seanshubin.detangler.zip.{ZipContents, ZipContentsIterator}

class ZipScannerImpl(files: FilesContract,
                     isCompressed: String => Boolean,
                     acceptName: String => Boolean,
                     warnNoRelevantClassesInPath: Path => Unit) extends ZipScanner {
  override def loadBytes(path: Path): Iterable[ScannedBytes] = {
    withInputStream(path) { inputStream =>
      val iterator = new ZipContentsIterator(inputStream, path.toString, isCompressed, acceptEntry)
      val bytesIterable = iterator.filter(zipContentsRelevant).map(_.bytes.toSeq).toIterable
      if (bytesIterable.isEmpty) {
        warnNoRelevantClassesInPath(path)
      }
      reifyBeforeStreamCloses(path, bytesIterable)
    }
  }

  private def zipContentsRelevant(zipContents: ZipContents): Boolean = {
    FileTypes.isClass(zipContents.zipEntry.getName)
  }

  private def reifyBeforeStreamCloses(path:Path, x: Iterable[Seq[Byte]]): Iterable[ScannedBytes] = {
    x.map(ScannedBytes(path.toString, _)).toIndexedSeq
  }

  private def withInputStream[T](path: Path)(f: InputStream => T): T = {
    val inputStream = files.newInputStream(path)
    try {
      f(inputStream)
    } finally {
      inputStream.close()
    }
  }

  def acceptEntry(path: Seq[String], entry: ZipEntry): Boolean = acceptName(entry.getName)
}
