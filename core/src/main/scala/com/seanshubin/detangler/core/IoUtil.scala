package com.seanshubin.detangler.core

import java.io.{InputStream, OutputStream}

import scala.annotation.tailrec

object IoUtil {
  @tailrec final def copyInputStreamToOutputStream(in: InputStream, out: OutputStream): Unit = {
    val ch = in.read()
    if (ch != -1) {
      out.write(ch)
      copyInputStreamToOutputStream(in, out)
    }
  }
}
