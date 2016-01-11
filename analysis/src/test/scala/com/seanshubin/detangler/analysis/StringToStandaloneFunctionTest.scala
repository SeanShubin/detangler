package com.seanshubin.detangler.analysis

import com.seanshubin.detangler.model.Standalone
import org.scalatest.FunSuite

class StringToStandaloneFunctionTest extends FunSuite {
  test("string to standalone") {
    val level = 3
    val includeStartsWithSeq = Seq(Seq("com", "seanshubin"), Seq("seanshubin"))
    val excludeStartsWithSeq = Seq(Seq("com", "seanshubin", "unchecked"))
    val dropStartsWithSeq = Seq(Seq("com", "seanshubin"), Seq("seanshubin"))
    val f = new StringToStandaloneFunction(level, includeStartsWithSeq, excludeStartsWithSeq, dropStartsWithSeq)
    assert(f("com/seanshubin/foo/bar/baz/Hello$baz") === Some(Standalone(Seq("foo", "bar/baz", "Hello"))))
    assert(f("seanshubin/foo/bar/baz/World$baz") === Some(Standalone(Seq("foo", "bar/baz", "World"))))
    assert(f("org/foo/bar/baz/Irrelevant$baz") === None)
    assert(f("com/seanshubin/Hi$baz") === Some(Standalone(Seq("-root-", "-root-", "Hi"))))
    assert(f("com/seanshubin/foo/bar/baz/Hello") === Some(Standalone(Seq("foo", "bar/baz", "Hello"))))
    assert(f("seanshubin/foo/bar/baz/World") === Some(Standalone(Seq("foo", "bar/baz", "World"))))
    assert(f("org/foo/bar/baz/Irrelevant") === None)
    assert(f("com/seanshubin/Hi") === Some(Standalone(Seq("-root-", "-root-", "Hi"))))
  }


  test("paths at 4 levels deep") {
    import StringToStandaloneFunction.standaloneAtLevel
    assert(standaloneAtLevel(4, Seq("a", "b", "c", "d", "e", "f")) === Standalone(Seq("a", "b", "c/d/e", "f")))
    assert(standaloneAtLevel(4, Seq("g", "h", "i", "j", "k")) === Standalone(Seq("g", "h", "i/j", "k")))
    assert(standaloneAtLevel(4, Seq("l", "m", "n", "o")) === Standalone(Seq("l", "m", "n", "o")))
    assert(standaloneAtLevel(4, Seq("p", "q", "r")) === Standalone(Seq("p", "q", "root-of-q", "r")))
    assert(standaloneAtLevel(4, Seq("s", "t")) === Standalone(Seq("s", "root-of-s", "root-of-s", "t")))
    assert(standaloneAtLevel(4, Seq("u")) === Standalone(Seq("-root-", "-root-", "-root-", "u")))
    assert(standaloneAtLevel(3, Seq("a", "b", "c", "d", "e", "f")) === Standalone(Seq("a", "b/c/d/e", "f")))
    assert(standaloneAtLevel(3, Seq("g", "h", "i", "j", "k")) === Standalone(Seq("g", "h/i/j", "k")))
    assert(standaloneAtLevel(3, Seq("l", "m", "n", "o")) === Standalone(Seq("l", "m/n", "o")))
    assert(standaloneAtLevel(3, Seq("p", "q", "r")) === Standalone(Seq("p", "q", "r")))
    assert(standaloneAtLevel(3, Seq("s", "t")) === Standalone(Seq("s", "root-of-s", "t")))
    assert(standaloneAtLevel(3, Seq("u")) === Standalone(Seq("-root-", "-root-", "u")))
    assert(standaloneAtLevel(2, Seq("a", "b", "c", "d", "e", "f")) === Standalone(Seq("a/b/c/d/e", "f")))
    assert(standaloneAtLevel(2, Seq("g", "h", "i", "j", "k")) === Standalone(Seq("g/h/i/j", "k")))
    assert(standaloneAtLevel(2, Seq("l", "m", "n", "o")) === Standalone(Seq("l/m/n", "o")))
    assert(standaloneAtLevel(2, Seq("p", "q", "r")) === Standalone(Seq("p/q", "r")))
    assert(standaloneAtLevel(2, Seq("s", "t")) === Standalone(Seq("s", "t")))
    assert(standaloneAtLevel(2, Seq("u")) === Standalone(Seq("-root-", "u")))
    assert(standaloneAtLevel(1, Seq("a", "b", "c", "d", "e", "f")) === Standalone(Seq("a/b/c/d/e/f")))
    assert(standaloneAtLevel(1, Seq("g", "h", "i", "j", "k")) === Standalone(Seq("g/h/i/j/k")))
    assert(standaloneAtLevel(1, Seq("l", "m", "n", "o")) === Standalone(Seq("l/m/n/o")))
    assert(standaloneAtLevel(1, Seq("p", "q", "r")) === Standalone(Seq("p/q/r")))
    assert(standaloneAtLevel(1, Seq("s", "t")) === Standalone(Seq("s/t")))
    assert(standaloneAtLevel(1, Seq("u")) === Standalone(Seq("u")))
  }
}
