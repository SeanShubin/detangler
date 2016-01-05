package com.seanshubin.detangler.scanner

import java.nio.file.Path
import java.time.Duration

import com.seanshubin.detangler.timer.Timer

class ScannerImpl(directoryScanner: DirectoryScanner,
                  fileScanner: FileScanner,
                  classBytesScanner: ClassBytesScanner,
                  notifyTimeTaken: (String, Duration) => Unit,
                  timer: Timer) extends Scanner {
  override def scanDependencies(): Iterable[(String, Seq[String])] = {
    val files: Iterable[Path] = directoryScanner.findFiles()
    def scanFile(file: Path): Iterable[Seq[Byte]] = {
      val (timeTaken, iterableOfBytes) = timer.measureTime {
        fileScanner.loadBytes(file)
      }
      notifyTimeTaken(s"scanned $file", timeTaken)
      iterableOfBytes
    }

    val (loadBytesTime, classBytesSeq) = timer.measureTime {
      files.flatMap(scanFile)
    }
    notifyTimeTaken("load bytes", loadBytesTime)

    val (parseClassesTime, dependencies) = timer.measureTime {
      classBytesSeq.map(classBytesScanner.parseDependencies)
    }
    notifyTimeTaken("parse classes", parseClassesTime)

    dependencies
  }
}
