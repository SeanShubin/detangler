package com.seanshubin.detangler.report

case class SetDifference[T](left: Set[T], right: Set[T]) {
  def isSame: Boolean = left == right

  def message: String = {
    val leftOnly = left -- right
    val rightOnly = right -- left
    s"$leftOnly $rightOnly"
  }
}
