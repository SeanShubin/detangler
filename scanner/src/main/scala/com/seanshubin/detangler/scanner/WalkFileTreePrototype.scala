package com.seanshubin.detangler.scanner

import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

object WalkFileTreePrototype extends App {
  val path = Paths.get(".")
  Files.walkFileTree(path, FooVisitor)

  object FooVisitor extends FileVisitor[Path] {
    override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = ???

    override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
      println(s"visitFile($file, $attrs)")
      FileVisitResult.CONTINUE

    }

    override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
      println(s"preVisitDirectory($dir, $attrs)")
      FileVisitResult.CONTINUE
    }

    override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
      println(s"postVisitDirectory($dir, $exc)")
      FileVisitResult.CONTINUE
    }
  }

}
