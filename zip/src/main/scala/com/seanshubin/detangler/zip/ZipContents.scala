package com.seanshubin.detangler.zip

import java.util.zip.ZipEntry

case class ZipContents(path: Seq[String], zipEntry: ZipEntry, bytes: Array[Byte])
