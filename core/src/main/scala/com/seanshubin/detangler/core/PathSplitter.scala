package com.seanshubin.detangler.core

class PathSplitter(pattern:String) {
  val RegexPattern = pattern.r
  def split(s:String):Seq[String] = {
    RegexPattern.unapplySeq(s) match {
      case Some(list) => list
      case None => throw new RuntimeException(s"value '$s' did not match pattern '$pattern'")
    }
  }
}
