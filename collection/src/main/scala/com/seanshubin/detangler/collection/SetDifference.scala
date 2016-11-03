package com.seanshubin.detangler.collection

object SetDifference {
  def diff[T](a: Set[T], b: Set[T]): DifferenceResult = {
    if (a == b) {
      val messageLines = Seq("Sets are identical") ++ a.toSeq.map("  " + _)
      val isSame = true
      DifferenceResult(isSame, messageLines)
    } else {
      val onlyA = a -- b
      val onlyB = b -- a
      val messageLines = Seq("Sets are different") ++ composeMessage("in a but not in b", onlyA) ++ composeMessage("in b but not in a", onlyB)
      val isSame = false
      DifferenceResult(isSame, messageLines)
    }
  }

  private def composeMessage[T](header: String, values: Set[T]): Seq[String] = {
    if (values.isEmpty) Seq() else Seq(header) ++ values.toSeq.map("  " + _)
  }
}
