package com.seanshubin.detangler.core

case class Arrow(from: UnitId, to: UnitId, reasons: Seq[Arrow])
